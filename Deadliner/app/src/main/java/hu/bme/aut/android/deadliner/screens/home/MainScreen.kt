package hu.bme.aut.android.deadliner.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.bme.aut.android.deadliner.screens.calendar.Calendar
import hu.bme.aut.android.deadliner.screens.common.Menu
import hu.bme.aut.android.deadliner.screens.common.MenuItemUiModel
import hu.bme.aut.android.deadliner.screens.common.TodoList
import hu.bme.aut.android.deadliner.screens.todo.TodoListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
               onMenuItemClick: (String) -> Unit,
               viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
) {
    var expandedMenu by remember { mutableStateOf(false) }
    val state by viewModel.state.collectAsState()

    // Your main screen layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "AI Deadline Planner") },
                actions = {
                    // You can add buttons to open the menu here
                    IconButton(onClick = { expandedMenu = !expandedMenu }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            //horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /*
            Image(
                painter = painterResource(id = R.drawable.logo), // Replace with your Android logo resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            */

            Calendar(
                modifier = Modifier
                    .weight(1f) // Take available vertical space
                    .padding(16.dp),
                onDateSelected = { viewModel.loadTodos(it) }
            )
            Column(
                modifier = Modifier
                    .weight(3f) // Take available vertical space
                    .fillMaxSize()
            ) {
                when (state) {
                    is MainState.Loading -> {
                        // TODO: Loading state UI
                    }
                    is MainState.Error -> {
                        // TODO: Error state UI
                    }
                    is MainState.Result -> {
                        val todolist = (state as MainState.Result).todoList
                        TodoList(
                            todos = todolist
                        )
                    }
                }
            }
        }



        Box(modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
            .padding(5.dp)) {
            Menu(
                expanded = expandedMenu,
                items = MenuItemUiModel.values(),
                onDismissRequest = { expandedMenu = false },
                onClick = {
                    onMenuItemClick(it)
                    expandedMenu = false
                },
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(onMenuItemClick = {})
}
