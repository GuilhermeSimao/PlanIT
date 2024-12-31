package pt.iade.planit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(eventId: Int, viewModel: EventDetailsViewModel, navController: NavController) {
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.fetchEventDetails(eventId, {
            isLoading = false
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

            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Imagem do evento
                    Image(
                        painter = rememberAsyncImagePainter(eventDetails.photoUrl),
                        contentDescription = "Event Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Título do evento
                    Text(
                        text = eventDetails.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    // Data e hora
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = eventDetails.date, style = MaterialTheme.typography.bodyMedium)
                    }

                    // Localização
                    eventDetails.location?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Place, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = it.address, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Descrição
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

                // Participantes
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
                        Icon(Icons.Default.Person, contentDescription = null)
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

                // Botão "Gerir Participantes"
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
        }
    }
}
