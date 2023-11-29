package hu.bme.aut.android.deadliner.screens.todo

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


sealed class TodoListState {
    object Loading : TodoListState()
    data class Error(val error: Throwable) : TodoListState()
    data class Result(val todoList : List<Todo>) : TodoListState()
}


class TodoListViewModel(private val todoService: FirebaseTodoService) : ViewModel() {

        private val _state = MutableStateFlow<TodoListState>(TodoListState.Loading)
        val state = _state.asStateFlow()

        init {
            //loadTestTodos()
           // if(todoService.todos == null)
                loadTodos()
        }

    private fun loadTestTodos() {
        viewModelScope.launch {
            try {
                _state.value = TodoListState.Loading
                delay(2000)
                //TODO: Add todo loading logic
                _state.value = TodoListState.Result(
                    todoList = listOf(
                        Todo(
                            id = "",
                            title = "Teszt feladat 1",
                            priority = Priority.High,
                            description = "Feladat leírás 1",
                            date = Timestamp.now(),
                            deadline = Timestamp.now(),
                            length = 80,
                            notify = false
                        ),
                        Todo(
                            id = "",
                            title = "Teszt feladat 2",
                            priority = Priority.Mid,
                            description = "Feladat leírás 2",
                            date = Timestamp.now(),
                            deadline = Timestamp.now(),
                            length = 50,
                            notify = false
                        ),
                        Todo(
                            id = "",
                            title = "Teszt feladat 3",
                            priority = Priority.Low,
                            description = "Feladat leasdfsadfdsffjksdhfjsdhfjksdhfjshdfjhsdjkfhsdjfhsdjfhsdjfsdjhfjsdhfjksdhfjkaHDFHKASG DFZIASDBFHASDGFHGSDAFKGSDBFHGASDF KJSDHFKASDUFHASDKFGASDHKGFJHASDGFJKASDGFJKASDGFSDJFZSDCVZJSDGBFHKSDGFAHSDGFSDGFHGRHARírás 3",
                            date = Timestamp.now(),
                            deadline = Timestamp.now(),
                            length = 800,
                            notify = false

                        ),
                    ),
                )
            } catch (e: Exception) {
                _state.value = TodoListState.Error(e)
            }
        }
    }


    private fun loadTodos() {
        viewModelScope.launch {
            _state.update { TodoListState.Loading }
            try {
                todoService.todos.collect {
                    val todos = it
                    _state.update {TodoListState.Result(todos) }
                }
            } catch (e: Exception) {
                _state.update { TodoListState.Error(e)}
            }
        }
    }

    fun deleteTodo(id: String) {
        viewModelScope.launch {
            try {
                todoService.deleteTodo(id)
            } catch (e: Exception) {
                // Handle the exception
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                TodoListViewModel(TodoApplication.todoService)
            }
        }
    }

}