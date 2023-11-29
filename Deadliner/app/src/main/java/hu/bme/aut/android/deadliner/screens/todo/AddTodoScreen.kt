package hu.bme.aut.android.deadliner.screens.todo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import hu.bme.aut.android.deadliner.data.common.Priority
import hu.bme.aut.android.deadliner.data.common.Todo
import kotlinx.coroutines.ExperimentalCoroutinesApi import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Alignment
import kotlinx.datetime.LocalDate
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toKotlinLocalDate
import java.time.LocalDateTime


@OptIn(ExperimentalComposeUiApi::class, ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AddTodoScreen(
    onBackClick: () -> Unit,
    preFilledTodo: Todo? = null,
    viewModel: AddTodoViewModel = viewModel(factory = AddTodoViewModel.Factory)
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(Date()) }
    var deadline by remember { mutableStateOf(Date()) }
    var length by remember { mutableStateOf(60) }
    var priority by remember { mutableStateOf(Priority.Low) }
    var notify by remember { mutableStateOf(false) }

    var isDatePickerDialogVisible by remember { mutableStateOf(false) }
    // set the initial date
    val datePickerState = rememberDatePickerState()

    // Get the context and resources
    val context = LocalContext.current

    if (preFilledTodo != null) {
        title = preFilledTodo.title
        description = preFilledTodo.description
        date = preFilledTodo.date.toDate()
        deadline = preFilledTodo.deadline.toDate()
        length = preFilledTodo.length
        priority = preFilledTodo.priority
        notify = preFilledTodo.notify
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Todo") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val todoToSave = Todo(
                            id = preFilledTodo?.id ?: "", // Use existing ID if preFilledTodo is provided
                            title = title,
                            description = description,
                            date = Timestamp(date),
                            deadline = Timestamp(deadline),
                            length = length *60,
                            priority = priority,
                            notify = notify
                        )

                        if (preFilledTodo == null) {
                            // Save a new todo
                            viewModel.scheduleTodo(todoToSave)
                        } else {
                            // Update an existing todo
                            viewModel.updateTodo(todoToSave)
                        }

                        onBackClick()
                    }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { paddingValues ->
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Todo Input fields go here
            // ...
            TodoInputField(
                label = "Title",
                value = title,
                onValueChange = { title = it }
            )

            TodoInputField(
                label = "Description",
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .weight(4f)
            )

            TodoInputField(
                label = "Length",
                value = length.toString(),
                onValueChange = { length = it.toIntOrNull() ?: 0 }
            )

            TodoInputField(
                label = "Selected Date",
                value = SimpleDateFormat("dd/MM/yyyy").format(deadline),
                onValueChange = {  },
                editable = false
            )

            // Deadline DatePicker
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Button(
                    onClick = {
                        isDatePickerDialogVisible = true
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .heightIn(min = 48.dp)
                ) {
                    Text("Select Date")
                }
            }

            // Priority RadioButtons
            PriorityRadioButtons(
                selectedPriority = priority,
                onPrioritySelected = { priority = it }
            )


            if (isDatePickerDialogVisible) {
                DatePickerDialog(
                    onDismissRequest = { isDatePickerDialogVisible = false },
                    confirmButton = {
                        TextButton(onClick = {
                            // Set the selected date to the deadline
                            deadline = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
                            isDatePickerDialogVisible = false
                        }) {
                            Text(text = "Confirm", color = MaterialTheme.colorScheme.secondary) // Set text color
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isDatePickerDialogVisible = false
                        }) {
                            Text(text = "Cancel", color = MaterialTheme.colorScheme.primary) // Set text color
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DatePicker(
                            state = datePickerState

                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TodoInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    editable: Boolean = true, // Add a new parameter for editability
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (editable) onValueChange(it) },
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        readOnly = !editable // Set readOnly based on the editability parameter
    )
}

@Composable
fun SaveButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Save")
    }
}

@Composable
fun PriorityRadioButtons(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    val priorities = Priority.values()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text("Priority", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            priorities.forEach { priority ->
                RadioButton(
                    selected = priority == selectedPriority,
                    onClick = { onPrioritySelected(priority) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(priority.name, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddTodoScreenPreview() {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = {  },
        confirmButton = {
            Button(onClick = {
            }, modifier = Modifier.wrapContentSize()) {
                Text(text = "Confirm") // Set text color
            }
        },
        dismissButton = {
            Button(onClick = {

            }) {
                Text(text = "Cancel") // Set text color
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DatePicker(state = (datePickerState)
            )
        }
    }
}
