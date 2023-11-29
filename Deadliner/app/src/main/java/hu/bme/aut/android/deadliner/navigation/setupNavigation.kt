package hu.bme.aut.android.deadliner.navigation

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hu.bme.aut.android.deadliner.data.common.Todo
import hu.bme.aut.android.deadliner.screens.PlaceholderScreen
import hu.bme.aut.android.deadliner.screens.fireBaseLogin.LoginScreen
import hu.bme.aut.android.deadliner.screens.fireBaseLogin.RegisterScreen
import hu.bme.aut.android.deadliner.screens.home.MainScreen
import hu.bme.aut.android.deadliner.screens.pomodoro.PomodoroTimerScreen
import hu.bme.aut.android.deadliner.screens.todo.AddTodoScreen
import hu.bme.aut.android.deadliner.screens.todo.EditTodoScreen
import hu.bme.aut.android.deadliner.screens.todo.TodoList

@Composable
fun setupNavigation(navController: NavHostController) {
    val TODO_ID_KEY = "todoId"
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(onMenuItemClick = { navController.navigate(it) })
        }
        composable("deadlinePlanner") {
            // Add the Deadline Planner screen Composable
        }
        composable("pomodoroTimer") {
            // Add the Pomodoro Timer screen Composable
        }
        // Add more composable destinations for other features
        composable(Screen.Todo.route) {
            TodoList( onEditClick = { todoId ->
                navController.navigate("${Screen.EditTodo.route}/$todoId") },
                onBackClick = {navController.navigate(Screen.MainScreen.route)},
                onAddClick = {navController.navigate(Screen.AddTodo.route)})
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Screen.MainScreen.route) },
                onNavigateToRegister = { navController.navigate(Screen.Register.route) })
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = { navController.navigate(Screen.MainScreen.route) },
                onNavigateToLogin = { navController.navigate(Screen.Login.route) })
        }

        composable(Screen.AddTodo.route) {
            AddTodoScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("${Screen.EditTodo.route}/{${TODO_ID_KEY}}") { backStackEntry ->
            // Retrieve the Todo ID from the arguments
            val todoId = backStackEntry.arguments?.getString(TODO_ID_KEY)
            if (todoId != null) {
                EditTodoScreen(
                    todoId = todoId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        composable(Screen.Pomodoro.route) {
            PomodoroTimerScreen()
        }

    }
}
