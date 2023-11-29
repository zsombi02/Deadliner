package hu.bme.aut.android.deadliner.screens.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PomodoroTimerScreen() {
    var time by remember { mutableStateOf(60) }
    var isRunning by remember { mutableStateOf(false) }
    var job by remember { mutableStateOf<Job?>(null) }

    val scope = rememberCoroutineScope()
    var userInput by remember { mutableStateOf("1") } // Default to 1 minute

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var minutes by remember { mutableStateOf(1) }
        var seconds by remember { mutableStateOf(0) }

        OutlinedTextField(
            value = userInput,
            onValueChange = {
                userInput = it
                // Validate and update the time when the input changes
                time = (it.toIntOrNull() ?: 0) * 60
            },
            label = { Text("Enter Time in Minutes") },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        DisposableEffect(isRunning) {
            if (isRunning) {
                job = scope.launch {
                    while (time > 0) {
                        delay(1000)
                        time--
                        minutes = time / 60
                        seconds = time % 60
                    }
                }
            } else {
                job?.cancel()
            }

            onDispose {
                job?.cancel()
            }
        }

        TimerDisplay(minutes, seconds)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    isRunning = !isRunning
                },
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
            ) {
                Icon(
                    if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isRunning) "Pause" else "Resume")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    time = (userInput.toIntOrNull() ?: 0) *60
                    //isRunning = false
                },
                modifier = Modifier
                    .padding(16.dp)
                    .wrapContentSize()
            ) {
                Icon(
                    Icons.Default.RestartAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Restart")
            }
        }
    }
}



@Composable
fun TimerDisplay(minutes: Int, seconds: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Timer,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
        PomodoroTimerScreen()
}