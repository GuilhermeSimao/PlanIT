package pt.iade.planit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
            TopAppBar(
                title = { Text("Detalhes do Evento") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
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

            LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
                item {
                    Text(eventDetails.title, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(eventDetails.description, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))

                    if (eventDetails.photoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(eventDetails.photoUrl),
                            contentDescription = "Event Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Criado por: ${eventDetails.userName}", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Participantes:", style = MaterialTheme.typography.headlineSmall)
                }

                items(eventDetails.participants) { participant ->
                    Text("${participant.userName} (${participant.status})", style = MaterialTheme.typography.bodySmall)
                }

                if (eventDetails.location != null) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Localização: ${eventDetails.location.address}")
                        // ADICIONAR MAPA
                    }
                }
            }
        }
    }
}
