package hu.bme.aut.android.deadliner.navigation

sealed class Screen(val route: String) {
    object MainScreen: Screen(route = "main")
    object Todo: Screen(route = "todo")
    object Settings: Screen(route = "settings")
    object Login: Screen(route = "login")
    object Register: Screen(route = "register")

    object AddTodo: Screen(route = "addTodo")

    object EditTodo: Screen(route = "editTodo")

    object Pomodoro: Screen(route = "pomodoro")


}