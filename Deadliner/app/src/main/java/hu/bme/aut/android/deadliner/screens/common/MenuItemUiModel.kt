package hu.bme.aut.android.deadliner.screens.common

import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import hu.bme.aut.android.deadliner.navigation.Screen

enum class MenuItemUiModel(
    val text: @Composable () -> Unit,
    val icon: @Composable () -> Unit,
    val screenRoute: String
) {
    TODO(
        text = { Text(text = "TODO")},
        icon = {
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Person, contentDescription = null)
        },
        screenRoute = Screen.Todo.route
    ),
    TIMER(
        text = { Text(text = "Timer")},
        icon = {
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Person, contentDescription = null)
        },
        screenRoute = Screen.Pomodoro.route
    ),
    STATISTICS(
        text = { Text(text = "Statistics")},
        icon = {
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Person, contentDescription = null)
        },
        screenRoute = "StatisticsScreen"
    ),
    PROFILE(
        text = { Text(text = "Profile")},
        icon = {
            Icon(imageVector = androidx.compose.material.icons.Icons.Default.Person, contentDescription = null)
        },
        screenRoute = "ProfileScreen"
    ),

}