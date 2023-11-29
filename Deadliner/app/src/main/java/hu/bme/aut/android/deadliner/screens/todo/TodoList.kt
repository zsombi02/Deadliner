package hu.bme.aut.android.deadliner.screens.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.deadliner.R
import hu.bme.aut.android.deadliner.data.common.Todo
import hu.bme.aut.android.deadliner.screens.common.TodoCard

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun TodoList(
    onEditClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    viewModel: TodoListViewModel = viewModel(factory = TodoListViewModel.Factory)
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    ) { contentPadding ->
        // Main content
        when (state) {
            is TodoListState.Loading -> {
                // TODO: Loading state UI
            }
            is TodoListState.Error -> {
                // TODO: Error state UI
            }
            is TodoListState.Result -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                ) {
                    items((state as TodoListState.Result).todoList) { todo ->
                        TodoCard(todo = todo, onEditClick = { onEditClick(todo.id) },
                            onDeleteClick = { viewModel.deleteTodo(todo.id)})
                    }
                }
            }

            else -> {}
        }
    }
}
@Preview
@Composable
fun TodoListPreview() {
    TodoList(onEditClick = {}, onBackClick = {}, onAddClick = {})
}
