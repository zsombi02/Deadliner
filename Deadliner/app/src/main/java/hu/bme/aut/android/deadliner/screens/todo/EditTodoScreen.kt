package hu.bme.aut.android.deadliner.screens.todo

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.deadliner.data.common.Todo

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun EditTodoScreen(
    todoId: String,
    onBackClick: () -> Unit,
    viewModel: AddTodoViewModel = viewModel(factory = AddTodoViewModel.Factory)
) {
    val preFilledTodo by viewModel.todo.collectAsState()

    DisposableEffect(todoId) {
        viewModel.getTodo(todoId)

        onDispose {
            // Cleanup if needed
        }
    }

    AddTodoScreen(
        onBackClick = onBackClick,
        viewModel = viewModel,
        preFilledTodo = preFilledTodo
    )
}