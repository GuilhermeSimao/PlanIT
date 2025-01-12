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
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import pt.iade.planit.api.GeocodingHelper
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

@Composable
fun CreateEventScreen(navController: NavController, loginViewModel: LoginViewModel, userId: Int) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) } // Agora para URI da imagem local
    var address by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf(0.0) }
    var longitude by remember { mutableStateOf(0.0) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Lançador para selecionar imagens do armazenamento
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { photoUri = it.toString() }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Create Event",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Date") },
                        placeholder = { Text("yyyy-MM-dd") },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(onClick = {
                        showDatePicker(context) { selectedDate -> date = selectedDate }
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Pick Date", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Time") },
                        placeholder = { Text("HH:mm") },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(onClick = {
                        showTimePicker(context) { selectedTime -> time = selectedTime }
                    }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Pick Time", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Address") },
                    placeholder = { Text("Enter event address") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botão para selecionar a imagem
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Image")
                }

                // Pré-visualização da imagem selecionada
                photoUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Button(
                        onClick = {
                            if (title.isNotBlank() && description.isNotBlank() && date.isNotBlank() && time.isNotBlank() && address.isNotBlank() && photoUri != null) {
                                isLoading = true
                                val dateTime = "${date}T$time"

                                loginViewModel.viewModelScope.launch {
                                    val coordinates = GeocodingHelper.getCoordinates(context, address)
                                    if (coordinates != null) {
                                        latitude = coordinates.first
                                        longitude = coordinates.second
                                        Log.d("CreateEventScreen", "Coordinates: latitude=$latitude, longitude=$longitude")
                                    } else {
                                        isLoading = false
                                        errorMessage = "Endereço inválido. Não foi possível obter as coordenadas."
                                        return@launch
                                    }

                                    Log.d("CreateEventScreen", "Address: $address")

                                    loginViewModel.createEvent(
                                        userId = userId,
                                        title = title,
                                        description = description,
                                        date = dateTime,
                                        photoUrl = photoUri,
                                        latitude = latitude,
                                        longitude = longitude,
                                        address = address,
                                        onSuccess = {
                                            isLoading = false
                                            navController.popBackStack()
                                        },
                                        onError = { error ->
                                            isLoading = false
                                            errorMessage = error
                                        }
                                    )
                                }
                            } else {
                                errorMessage = "Preencha todos os campos obrigatórios e selecione uma imagem."
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


