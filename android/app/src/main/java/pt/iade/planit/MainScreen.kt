package pt.iade.planit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.planit.api.Event
import coil.compose.AsyncImage
import androidx.compose.material.icons.filled.Event

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(id: Int?, loginViewModel: LoginViewModel, navController: NavController) {
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(id) {
        id?.let { userId ->
            loginViewModel.getUserEvents(userId) { result ->
                events = result
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
            .padding(8.dp)
            .clickable {
                navController.navigate(Screen.DetailScreen.withArgs(event.id.toString()))
            },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, style = MaterialTheme.typography.titleLarge)
            Text(event.description, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Text("Date: ${event.date}", style = MaterialTheme.typography.bodySmall)

            if (!event.photoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = event.photoUrl,
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyStateMessage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp), // Reserva espaço para os botões na parte inferior
        verticalArrangement = Arrangement.Center, // Centraliza verticalmente
        horizontalAlignment = Alignment.CenterHorizontally // Centraliza horizontalmente
    ) {
        Icon(Icons.Default.Event, contentDescription = "No Events", modifier = Modifier.size(80.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No events found. Create a new event!", style = MaterialTheme.typography.bodyLarge)
    }
}


