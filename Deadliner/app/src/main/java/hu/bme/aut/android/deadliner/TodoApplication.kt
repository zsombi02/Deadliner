package hu.bme.aut.android.deadliner

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseAuthService
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseTodoService

class TodoApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        authService = FirebaseAuthService(FirebaseAuth.getInstance())
        todoService = FirebaseTodoService(FirebaseFirestore.getInstance(), authService)
    }

    companion object{
        lateinit var authService: FirebaseAuthService
        lateinit var todoService: FirebaseTodoService
    }
}