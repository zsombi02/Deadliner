package hu.bme.aut.android.deadliner.screens.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.deadliner.TodoApplication
import hu.bme.aut.android.deadliner.data.common.Todo
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseTodoService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
sealed class MainState {
    object Loading : MainState()
    data class Error(val error: Throwable) : MainState()
    data class Result(val todoList: List<Todo>) : MainState()
}
class MainViewModel (private val todoService: FirebaseTodoService) : ViewModel(){
    /*
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos
    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate?> = _selectedDate
    private val _filteredtodos = MutableStateFlow<List<Todo>>(emptyList())
    val filteredtodos: StateFlow<List<Todo>> = _filteredtodos

     */

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow()

    init {
        loadTodos(LocalDate.now())
    }
    /*
    private fun loadTodos() {
        viewModelScope.launch {
            try {
                todoService.todos.collect {
                    _todos.value = it
                }
            } catch (e: Exception) {
                // Handle exception if needed
            }
        }
    }

     */

    fun loadTodos(date: LocalDate) {
        viewModelScope.launch {
            _state.update { MainState.Loading }
            try {
                todoService.todos.collect { todos ->
                    // Filter todos based on the provided date
                    val filteredTodos = todos.filter { todo ->
                        val todoDate = todo.date?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

                        todoDate?.isEqual(date) == true
                    }

                    // Update the state with the filtered todos
                    _state.update { MainState.Result(filteredTodos) }
                }
            } catch (e: Exception) {
                // Handle the exception if needed
                _state.update { MainState.Error(e) }
            }
        }
    }

    /*
    fun loadFilteredTodos() {
        val selectedDate = _selectedDate.value

        _filteredtodos.value = _todos.value.filter { todo ->
            val todoDate = todo.date?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

            todoDate?.isEqual(selectedDate) == true
        }
    }

    fun setSelectedDate(selectedDate: LocalDate) {
        _selectedDate.value = selectedDate
        loadFilteredTodos()
    }

     */


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MainViewModel(TodoApplication.todoService)
            }
        }
    }
}