package hu.bme.aut.android.deadliner.data.common.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import hu.bme.aut.android.deadliner.data.common.Priority
import hu.bme.aut.android.deadliner.data.common.Todo

data class FirebaseTodo(
    @DocumentId val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Timestamp = Timestamp.now(),
    val deadline: Timestamp = Timestamp.now(),
    val length: Int = -1,
    val priority: Priority = Priority.Low,
    val notify: Boolean = false
)
    fun FirebaseTodo.asTodo() = Todo(
        id = id,
        title = title,
        description = description,
        date = date,
        deadline = deadline,
        length = length ,
        priority = priority ,
        notify = notify
    )

    fun Todo.asFirebaseTodo() = FirebaseTodo(
        id = id,
        title = title,
        description = description,
        date = date,
        deadline = deadline,
        length = length ,
        priority = priority ,
        notify = notify
    )

