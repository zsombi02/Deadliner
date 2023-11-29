package hu.bme.aut.android.deadliner.screens.todo

import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.Timestamp
import hu.bme.aut.android.deadliner.TodoApplication
import hu.bme.aut.android.deadliner.data.common.Priority
import hu.bme.aut.android.deadliner.data.common.Todo
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseTodoService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class AddTodoViewModel constructor(
    private val todoService: FirebaseTodoService
) : ViewModel() {

    private val _todoList = MutableStateFlow<List<Todo>>(emptyList())
    val todoList: StateFlow<List<Todo>> = _todoList

    private val _todo = MutableStateFlow<Todo?>(null)
    val todo: StateFlow<Todo?> = _todo

    init {
        loadTodos()
    }

    fun saveTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    todoService.saveTodo(todo)
                }
            } catch (e: Exception) {
                // Handle saveTodo error
                Log.e("AddTodoViewModel", "Error saving todo: ${e.message}")
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
                    todoService.updateTodo(todo)
                }
            } catch (e: Exception) {
                // Handle updateTodo error
                Log.e("AddTodoViewModel", "Error updating todo: ${e.message}")
            }
        }
    }

    fun getTodo(id: String) {
        viewModelScope.launch {
            try {
                val loadedTodo = todoService.getTodo(id)
                _todo.value = loadedTodo
            } catch (e: Exception) {
                // Handle getTodo error
                Log.e("AddTodoViewModel", "Error getting todo: ${e.message}")
            }
        }
    }

    // Inside AddTodoViewModel

    fun scheduleTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                val scheduledTodo = scheduleTodoInternal(todo)
                if (scheduledTodo != null) {
                    // Update the scheduled todo in the database
                    saveTodo(scheduledTodo)
                }
            } catch (e: Exception) {
                // Handle scheduling error
                Log.e("AddTodoViewModel", "Error scheduling todo: ${e.message}")
            }
        }
    }

    private suspend fun scheduleTodoInternal(todo: Todo): Todo? = suspendCoroutine { cont ->
        val existingTodos = todoList.value

        val scheduledDate = calculateScheduledDate(todo, existingTodos)

        if (scheduledDate != null) {
            val scheduledTodo = todo.copy(date = scheduledDate)
            cont.resume(scheduledTodo)
        } else {
            val alternativeScheduledDate = calculateAlternativeScheduledDate(todo, existingTodos)
            if (alternativeScheduledDate != null) {
                val alternativeScheduledTodo = todo.copy(date = alternativeScheduledDate)
                cont.resume(alternativeScheduledTodo)
            } else {
                cont.resume(null)
            }
        }
    }

    private fun calculateScheduledDate(todo: Todo, existingTodos: List<Todo>): Timestamp? {
        val currentDate = LocalDate.now()

        if (todo.priority == Priority.High) {
            val weekdaysBeforeWeekend = (currentDate.dayOfWeek.value..5).toList()
            val availableWeekdaysBeforeWeekend = weekdaysBeforeWeekend.filterNot { dayOfWeek ->
                existingTodos.any { it.date.toLocalDate().dayOfWeek.value == dayOfWeek }
            }

            if (availableWeekdaysBeforeWeekend.isNotEmpty()) {
                val nextAvailableWeekday = availableWeekdaysBeforeWeekend.first()
                val nextAvailableDate =
                    currentDate.plusDays((nextAvailableWeekday - currentDate.dayOfWeek.value).toLong())
                return nextAvailableDate.atStartOfDay().toLocalDate().toTimestamp()
            }
        }

        // Iterate through dates to find the next available date
        var nextAvailableDate = currentDate
        val maxIterations = 365 // Limit the number of iterations to avoid an infinite loop

        for (i in 1..maxIterations) {
            // Check if the sum of lengths on the same day doesn't exceed a dynamic limit
            val todosOnSameDay = existingTodos.filter { it.date.toLocalDate() == nextAvailableDate }
            val totalLengthOnSameDay = todosOnSameDay.sumOf { it.length }

            val dynamicLimit = calculateDynamicLimit(existingTodos)
            if (totalLengthOnSameDay + todo.length <= dynamicLimit) {
                return nextAvailableDate.atStartOfDay().toLocalDate().toTimestamp()
            }

            // Move to the next day
            nextAvailableDate = nextAvailableDate.plusDays(1)
        }

        // Couldn't find a suitable date after a reasonable number of iterations
        return null
    }

    private fun calculateDynamicLimit(existingTodos: List<Todo>): Int {
        val averageLength = existingTodos.map { it.length }.average().toInt()
        return (averageLength * 1.2).toInt() // Adjust the multiplier as needed
    }

    private fun calculateAlternativeScheduledDate(todo: Todo, existingTodos: List<Todo>): Timestamp? {
        val currentDate = LocalDate.now()

        val higherPriorityTasks = existingTodos.filter { it.priority > todo.priority }

        if (higherPriorityTasks.isNotEmpty()) {
            // Sort higher priority tasks by deadline ascending
            val sortedTasks = higherPriorityTasks.sortedBy { it.deadline.toLocalDate() }

            // Find the task with the least amount of total length
            val taskWithLeastLength = sortedTasks.minByOrNull {
                existingTodos.filter { existing -> existing.priority == it.priority }
                    .sumOf { existing -> existing.length }
            }

            taskWithLeastLength?.let {
                // Schedule the todo on the first day within the deadline of the task with least length
                val earliestDeadline = it.deadline.toLocalDate()
                if (currentDate < earliestDeadline) {
                    return currentDate.atStartOfDay().toLocalDate().toTimestamp()
                }
            }
        }
        return null
    }

    private fun findNextAvailableWeekend(currentDate: LocalDate): LocalDate? {
        var nextDate = currentDate.plusDays(1)
        while (nextDate.dayOfWeek !in setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
            nextDate = nextDate.plusDays(1)
        }
        return nextDate
    }

    fun LocalDate.toTimestamp(): Timestamp {
        val date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
        return Timestamp(date)
    }

    fun Timestamp.toLocalDate(): LocalDate {
        val instant = this.toDate().toInstant()
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            try {
                todoService.todos.collect {
                    val todos = it
                    _todoList.value = todos
                }
            } catch (e: Exception) {
                // Handle loadTodos error
                Log.e("AddTodoViewModel", "Error loading todos: ${e.message}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AddTodoViewModel(TodoApplication.todoService)
            }
        }
    }
}
