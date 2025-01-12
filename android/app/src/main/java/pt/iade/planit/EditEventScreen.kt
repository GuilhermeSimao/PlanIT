package pt.iade.planit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import pt.iade.planit.api.GeocodingHelper
import pt.iade.planit.api.Event

@Composable
fun EditEventScreen(
    eventId: Int,
    viewModel: EventDetailsViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
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

    // Carregar os detalhes do evento
    LaunchedEffect(eventId) {
        viewModel.fetchEventDetails(eventId, onSuccess = {
            val eventDetails = viewModel.eventDetails!!
            title = eventDetails.title
            description = eventDetails.description
            date = eventDetails.date.substringBefore("T")
            time = eventDetails.date.substringAfter("T")
            photoUri = eventDetails.photoUrl
            address = eventDetails.address ?: ""
            latitude = eventDetails.latitude ?: 0.0
            longitude = eventDetails.longitude ?: 0.0
        }, onError = {
            errorMessage = it
        })
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Editar Evento",
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
                    label = { Text("Título") },
                    placeholder = { Text("Título do evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descrição") },
                    placeholder = { Text("Descrição breve do evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = date,
                        onValueChange = {},
                        label = { Text("Data") },
                        placeholder = { Text("yyyy-MM-dd") },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(onClick = {
                        showDatePicker(context) { selectedDate -> date = selectedDate }
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Selecionar Data", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = time,
                        onValueChange = {},
                        label = { Text("Hora") },
                        placeholder = { Text("HH:mm") },
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(onClick = {
                        showTimePicker(context) { selectedTime -> time = selectedTime }
                    }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Selecionar Hora", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Endereço") },
                    placeholder = { Text("Digite o endereço do evento") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Selecionar Imagem")
                }

                photoUri?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = "Imagem selecionada",
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
                            isLoading = true
                            val dateTime = if (date.isNotBlank() && time.isNotBlank()) "${date}T${time}" else viewModel.eventDetails!!.date

                            viewModel.viewModelScope.launch {
                                // Obtenha as coordenadas apenas se o endereço foi modificado
                                val coordinates = if (address.isNotBlank() && address != viewModel.eventDetails!!.address) {
                                    GeocodingHelper.getCoordinates(context, address)
                                } else {
                                    // Use as coordenadas existentes do evento
                                    Pair(viewModel.eventDetails!!.latitude, viewModel.eventDetails!!.longitude)
                                }

                                // Validação das coordenadas
                                if (coordinates != null) {
                                    latitude = (coordinates.first ?: viewModel.eventDetails!!.latitude)!!
                                    longitude = (coordinates.second ?: viewModel.eventDetails!!.longitude)!!
                                } else if (address.isNotBlank() && address != viewModel.eventDetails!!.address) {
                                    isLoading = false
                                    errorMessage = "Endereço inválido. Não foi possível obter as coordenadas."
                                    return@launch
                                }

                                // Atualize o evento com as novas coordenadas ou valores existentes
                                val updatedEvent = Event(
                                    id = eventId,
                                    userId = viewModel.eventDetails!!.userId?.toInt() ?: 0,
                                    title = if (title.isNotBlank()) title else viewModel.eventDetails!!.title,
                                    description = if (description.isNotBlank()) description else viewModel.eventDetails!!.description,
                                    date = dateTime,
                                    photoUrl = photoUri ?: viewModel.eventDetails!!.photoUrl.orEmpty(),
                                    latitude = latitude,
                                    longitude = longitude,
                                    address = if (address.isNotBlank()) address else viewModel.eventDetails!!.address.orEmpty()
                                )

                                viewModel.updateEvent(
                                    eventId,
                                    updatedEvent,
                                    onSuccess = {
                                        isLoading = false
                                        navController.navigate(Screen.DetailScreen.withArgs(eventId.toString(), viewModel.eventDetails!!.userId.toString())) {
                                            popUpTo(Screen.DetailScreen.route) { inclusive = true }
                                        }
                                    },
                                    onError = { error ->
                                        isLoading = false
                                        errorMessage = error
                                    }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar Alterações")
                    }
                }

                if (errorMessage.isNotBlank()) {
                    Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
