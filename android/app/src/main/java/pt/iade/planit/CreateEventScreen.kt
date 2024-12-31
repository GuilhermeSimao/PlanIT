package pt.iade.planit

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.platform.LocalContext
import java.util.*

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
        onDateSelected(formattedDate)
    }, year, month, day).show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        onTimeSelected(formattedTime)
    }, hour, minute, true).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavController, loginViewModel: LoginViewModel, userId: Int) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var photoUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Event") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center // Centraliza o formulário vertical e horizontalmente
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center // Alinha o conteúdo ao centro verticalmente
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    placeholder = { Text("Event title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    placeholder = { Text("Short description about the event") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botão para escolher a data
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        placeholder = { Text("yyyy-MM-dd") },
                        modifier = Modifier.weight(1f),
                        enabled = false // Apenas exibido
                    )
                    IconButton(onClick = {
                        showDatePicker(context) { selectedDate ->
                            date = selectedDate
                        }
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Pick Date")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botão para escolher a hora
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Time") },
                        placeholder = { Text("HH:mm") },
                        modifier = Modifier.weight(1f),
                        enabled = false // Apenas exibido
                    )
                    IconButton(onClick = {
                        showTimePicker(context) { selectedTime ->
                            time = selectedTime
                        }
                    }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Pick Time")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = photoUrl,
                    onValueChange = { photoUrl = it },
                    label = { Text("Photo URL") },
                    placeholder = { Text("Optional image URL") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                                isLoading = true
                                val dateTime = "${date}T$time"
                                loginViewModel.createEvent(
                                    userId, title, description, dateTime, photoUrl
                                ) {
                                    isLoading = false
                                    navController.popBackStack()
                                }
                            } else {
                                errorMessage = "Please fill in all required fields."
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Create Event")
                    }
                }

                if (errorMessage.isNotBlank()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
