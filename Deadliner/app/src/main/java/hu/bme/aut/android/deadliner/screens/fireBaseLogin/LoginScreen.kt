package hu.bme.aut.android.deadliner.screens.fireBaseLogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.deadliner.MainActivity
import hu.bme.aut.android.deadliner.data.common.firebase.FirebaseAuthService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)
) {
    // State for email and password input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Collect the result from the ViewModel
    val loginResult by viewModel.loginResult.collectAsState()


    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Login Button
        Button(
            onClick = {
                viewModel.login(email, password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }

        // Navigate to Register Screen
        TextButton(onClick = onNavigateToRegister) {
            Text("Don't have an account? Register")
        }

        // Display login result
        loginResult?.let {
            if (it.success) {
                onLoginSuccess()
            } else {
                Text(it.error ?: "", color = Color.Red)
            }
        }
    }
}

// RegisterScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = RegisterViewModel.Factory)
) {
    // State for email, password, and confirmPassword input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Collect the result from the ViewModel
    val registerResult by viewModel.registerResult.collectAsState()

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email Input
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        // Password Input
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Confirm Password Input
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            visualTransformation = PasswordVisualTransformation()
        )

        // Register Button
        Button(
            onClick = {
                viewModel.register(email, password, confirmPassword)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Register")
        }

        // Navigate to Login Screen
        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Login")
        }

        // Display register result
        registerResult?.let {
            if (it.success) {
                onRegisterSuccess()
            } else {
                Text(it.error ?: "", color = Color.Red)
            }
        }
    }
}

