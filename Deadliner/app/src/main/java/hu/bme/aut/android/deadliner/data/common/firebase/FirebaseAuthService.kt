package hu.bme.aut.android.deadliner.data.common.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.deadliner.data.common.User
import kotlinx.coroutines.channels.awaitClose  
import kotlinx.coroutines.flow.Flow  
import kotlinx.coroutines.flow.callbackFlow  
import kotlinx.coroutines.tasks.await

class FirebaseAuthService(private val firebaseAuth: FirebaseAuth) {
     val currentUserId: String? get() = firebaseAuth.currentUser?.uid
     val hasUser: Boolean get() = firebaseAuth.currentUser != null
     val currentUser: Flow<User?>
        get() = callbackFlow {
            this.trySend(currentUserId?.let { User(it) })
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) })
                }
            firebaseAuth.addAuthStateListener(listener)
            awaitClose { firebaseAuth.removeAuthStateListener(listener) }
        }

    //P7XRW7
     suspend fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val user = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(user?.email?.substringBefore('@'))
                    .build()
                user?.updateProfile(profileChangeRequest)
            }.await()
    }

    suspend fun authenticate(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun sendRecoveryEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }

    suspend fun deleteAccount() {
        firebaseAuth.currentUser!!.delete().await()
    }

    suspend fun signOut() {
        firebaseAuth.signOut()
    }
}