package pt.iade.planit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.text.SimpleDateFormat
import java.util.Locale

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        date?.let { outputFormat.format(it) } ?: dateString
    } catch (e: Exception) {
        dateString
    }
}

@Composable
fun DetailScreen(
    eventId: Int,
    currentUserId: Int,
    viewModel: EventDetailsViewModel,
    navController: NavController
) {
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchEventDetails(eventId, {
            isLoading = false
            println("Criador do evento: ${viewModel.eventDetails?.userId}")
        }, { error ->
            isLoading = false
            errorMessage = error
        })
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Detalhes do Evento",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage.isNotBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        } else {
            val eventDetails = viewModel.eventDetails!!

            println("Criador do evento: ${eventDetails.userId}, Usuário atual: $currentUserId")

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Image(
                        painter = rememberAsyncImagePainter(eventDetails.photoUrl),
                        contentDescription = "Event Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = eventDetails.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = formatDate(eventDetails.date),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    eventDetails.location?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = it.address ?: "No Address Available",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Descrição:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = eventDetails.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                item {
                    Text(
                        text = "Participantes:",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(eventDetails.participants) { participant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = participant.userName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = participant.status,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                if (eventDetails.userId == currentUserId.toDouble()) {
                    item {
                        Button(
                            onClick = {
                                navController.navigate(Screen.ManageParticipants.withArgs(eventId.toString()))
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Gerir Participantes")
                        }
                    }
                }

                item {
                    eventDetails.latitude?.let { lat ->
                        eventDetails.longitude?.let { lng ->
                            Spacer(modifier = Modifier.height(16.dp))
                            GoogleMapView(latitude = lat, longitude = lng)
                        }
                    } ?: run {
                        Text(
                            text = "Localização indisponível.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun GoogleMapView(latitude: Double, longitude: Double) {
    val location = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        GoogleMap(
            modifier = Modifier.matchParentSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = rememberMarkerState(position = location),
                title = "Local do Evento",
                snippet = "Latitude: $latitude, Longitude: $longitude"
            )
        }
    }
}

