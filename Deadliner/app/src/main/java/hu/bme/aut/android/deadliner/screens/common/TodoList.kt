package hu.bme.aut.android.deadliner.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import hu.bme.aut.android.deadliner.data.common.Priority
import hu.bme.aut.android.deadliner.data.common.Todo
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.lazy.items


@Composable
fun TodoList(todos: List<Todo>) {
    LazyColumn {
        items(todos) { todo ->
            TodoItem(todo = todo)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { /* Handle item click if needed */ }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(getPriorityColor(todo.priority))
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = todo.title,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${todo.length % 60} minutes left",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Priority: ${todo.priority.name}",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Function to get the color based on Priority
@Composable
private fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.Low -> Color(176, 224, 230) // Light Turquoise
        Priority.Mid -> Color(255, 218, 185) // Peach
        Priority.High -> Color(240, 128, 128) // Light Coral
    }
}
