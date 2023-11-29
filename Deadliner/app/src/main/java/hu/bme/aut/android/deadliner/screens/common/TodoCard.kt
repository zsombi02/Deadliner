package hu.bme.aut.android.deadliner.screens.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import hu.bme.aut.android.deadliner.data.common.Priority
import hu.bme.aut.android.deadliner.data.common.Todo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun TodoCard(todo: Todo,
             onEditClick: () -> Unit,
             onDeleteClick: () -> Unit)
{
    var expanded by remember { mutableStateOf (false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = {expanded = !expanded})
            .animateContentSize()
    )
     {
        // Card content goes here, at this state a column to hold items
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
                         .height(16.dp).width(48.dp)
                         .background(getPriorityColor(todo.priority))
                 )

                 Spacer(modifier = Modifier.width(8.dp))


                 Text(
                     text = todo.title,
                     color = MaterialTheme.colorScheme.primary,
                     style = MaterialTheme.typography.titleMedium
                 )

                 if(!expanded) {
                     Text(
                         text = formatDate(todo.date),
                         color = Color.Gray,
                         style = MaterialTheme.typography.bodyMedium
                     )

                     Text(
                         text = todo.priority.name,
                         color = Color.Gray,
                         style = MaterialTheme.typography.bodyMedium
                     )
                 }
             }

            if(expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = " Deadline: ${formatDate(todo.deadline)}",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = "${todo.length%60} minutes left",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { onEditClick() },
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    }
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.error)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }

            }

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

@Composable
private fun formatDate(date: Timestamp): String {
    val dateFormat = SimpleDateFormat("MM dd", Locale.getDefault())
    return dateFormat.format(date.toDate())
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TodoCardPreview() {
    val todo = Todo(
        id = "",
        title = "Sample Todo",
        description = "This is a sample description.",
        date = Timestamp.now(),
        deadline = Timestamp.now(),
        length = 30,
        priority = Priority.High,
        notify = true
    )

    TodoCard(todo = todo, onEditClick = {}, onDeleteClick = {})
}
