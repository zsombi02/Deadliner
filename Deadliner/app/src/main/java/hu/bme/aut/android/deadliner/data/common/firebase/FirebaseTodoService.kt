package hu.bme.aut.android.deadliner.data.common.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.firestore.ktx.toObject
import hu.bme.aut.android.deadliner.data.common.Todo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.util.Date

class FirebaseTodoService(
    private val firestore: FirebaseFirestore,
    private val authService: FirebaseAuthService
){

    val todos: Flow<List<Todo>> = authService.currentUser.flatMapLatest { user ->
        if (user == null) flow { emit(emptyList()) }
        else currentCollection(user.id)
            .snapshots()
            .map { snapshot ->
                snapshot
                    .toObjects<FirebaseTodo>()
                    .map {
                        it.asTodo()
                    }
            }
    }

    suspend fun getTodo(id: String): Todo? =
        authService.currentUserId?.let {
            currentCollection(it).document(id).get().await().toObject<FirebaseTodo>()?.asTodo()
        }

    suspend fun saveTodo(todo: Todo) {
        authService.currentUserId?.let {
            currentCollection(it).add(todo.asFirebaseTodo()).await()
        }
    }

    suspend fun updateTodo(todo: Todo) {
        authService.currentUserId?.let {
            currentCollection(it).document(todo.id.toString()).set(todo.asFirebaseTodo()).await()
        }
    }

    suspend fun deleteTodo(id: String) {
        authService.currentUserId?.let {
            currentCollection(it).document(id).delete().await()
        }
    }


    private fun currentCollection(userId: String) =
        firestore.collection(USER_COLLECTION).document(userId).collection(TODO_COLLECTION)


    companion object {
        private const val USER_COLLECTION = "users"
        private const val TODO_COLLECTION = "todos"
    }


}