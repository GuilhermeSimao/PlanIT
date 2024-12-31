package pt.iade.planit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.planit.api.Event
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Event
import androidx.compose.ui.layout.ContentScale
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(id: Int?, loginViewModel: LoginViewModel, navController: NavController) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        id?.let { userId ->
            loginViewModel.getUserEvents(userId) { result ->
                events = result.sortedBy { event ->
                    try {
                        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(event.date)
                    } catch (e: Exception) {
                        null
                    }
                }
                loading = false
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Seus Eventos",
                showLogoutButton = true,
                onLogoutClick = { navController.navigate(Screen.Login.route) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (events.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    EmptyStateMessage()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(events) { event ->
                        EventCard(event, navController)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        id?.let { userId ->
                            navController.navigate(Screen.CreateEvent.withArgs(userId.toString()))
                        }
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Criar Evento")
                }

                Button(
                    onClick = {
                        id?.let { userId ->
                            navController.navigate(Screen.PendingInvites.withArgs(userId.toString()))
                        }
                    },
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Gerir Convites")
                }
            }
        }
    }
}


@Composable
fun EventCard(event: Event, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Imagem com largura total do Card
            if (!event.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = event.photoUrl,
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxWidth() // Garante largura total do card
                        .height(180.dp), // Altura fixa para manter o layout consistente
                    contentScale = ContentScale.Crop // Faz a imagem preencher toda a área
                )
            }

            // Detalhes do evento
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Descrição
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Data com ícone
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Event Date",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = formatEventDate(event.date),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Botão com largura total
            Button(
                onClick = {
                    navController.navigate(Screen.DetailScreen.withArgs(event.id.toString()))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("Ver mais")
            }
        }
    }
}

// Formatação da data para um formato mais amigável
fun formatEventDate(dateString: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString // Caso haja erro, retorna a string original
    }
}

@Composable
fun EmptyStateMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Event, contentDescription = "No Events", modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No events found. Create a new event!", style = MaterialTheme.typography.bodyLarge)
    }
}


