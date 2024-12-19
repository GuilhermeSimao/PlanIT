package pt.iade.planit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.planit.api.ParticipantResponse
import pt.iade.planit.viewmodel.ParticipantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingInvitesScreen(userId: Int, viewModel: ParticipantViewModel, navController: NavController) {
    var invites by remember { mutableStateOf<List<ParticipantResponse>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    println("Opening PendingInvitesScreen for userId: $userId")

    LaunchedEffect(Unit) {
        println("Loading invites for userId: $userId")
        viewModel.getPendingInvites(userId, { invites = it }, { errorMessage = it })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Convites Pendentes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (invites.isEmpty() && errorMessage.isBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Sem convites pendentes.")
            }
        } else if (errorMessage.isNotBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(invites) { invite ->
                    InviteItem(
                        invite = invite,
                        viewModel = viewModel,
                        onUpdate = {
                            viewModel.getPendingInvites(userId, { updatedInvites ->
                                invites = updatedInvites
                            }, { errorMessage = it })
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InviteItem(invite: ParticipantResponse, viewModel: ParticipantViewModel, onUpdate: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = invite.eventTitle, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Status: ${invite.status}", style = MaterialTheme.typography.bodySmall)
        }

        Row {
            Button(onClick = {
                viewModel.updateParticipantStatus(invite.id, "confirmed", {
                    onUpdate()
                }, {})
            }) {
                Text("Aceitar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                viewModel.updateParticipantStatus(invite.id, "declined", {
                    onUpdate()
                }, {})
            }) {
                Text("Recusar")
            }
        }
    }
}
