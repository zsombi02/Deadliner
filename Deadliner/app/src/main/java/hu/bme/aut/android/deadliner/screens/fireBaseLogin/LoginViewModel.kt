package hu.bme.aut.android.deadliner.screens.fireBaseLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import hu.bme.aut.android.deadliner.TodoApplication
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// LoginViewModel.kt
// LoginViewModel.kt
class LoginViewModel(private val authService: FirebaseAuthService) : ViewModel() {

    private val _loginResult = MutableStateFlow<AuthResult?>(null)
    val loginResult: StateFlow<AuthResult?> = _loginResult

    /*
    init {
        if (authService.currentUser != null) {
            _loginResult.value = AuthResult(success = true)
        }
    }

     */

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                authService.authenticate(email, password)
                _loginResult.value = AuthResult(success = true)
            } catch (e: Exception) {
                _loginResult.value = AuthResult(success = false, error = e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                LoginViewModel(
                  TodoApplication.authService
                )
            }
        }
    }
}

// RegisterViewModel.kt
class RegisterViewModel(private val authService: FirebaseAuthService) : ViewModel() {

    private val _registerResult = MutableStateFlow<AuthResult?>(null)
    val registerResult: StateFlow<AuthResult?> = _registerResult


    fun register(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch {
            try {
                if (password == confirmPassword) {
                    authService.signUp(email, password)
                    _registerResult.value = AuthResult(success = true)
                } else {
                    _registerResult.value = AuthResult(success = false, error = "Passwords do not match")
                }
            } catch (e: Exception) {
                _registerResult.value = AuthResult(success = false, error = e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                RegisterViewModel(
                    TodoApplication.authService
                )
            }
        }
    }
}

data class AuthResult(val success: Boolean, val error: String? = null)

