package hu.bme.aut.android.deadliner.data.common

import com.google.firebase.Timestamp
import java.util.Date

data class Todo(
    val id: String,
    val title: String,
    val description: String,
    val date: Timestamp,
    val deadline: Timestamp,
    val length: Int,
    val priority: Priority,
    val notify: Boolean
)

enum class Priority() {
    Low, Mid, High
}
