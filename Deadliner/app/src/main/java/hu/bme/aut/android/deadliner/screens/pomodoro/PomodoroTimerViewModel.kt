package hu.bme.aut.android.deadliner.screens.pomodoro

import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.deadliner.TodoApplication
import hu.bme.aut.android.deadliner.screens.todo.TodoListViewModel
import kotlinx.coroutines.CoroutineScope

class PomodoroTimerViewModel : ViewModel() {

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PomodoroTimerViewModel()
            }
        }
    }



}