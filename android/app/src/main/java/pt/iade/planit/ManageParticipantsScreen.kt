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
fun ManageParticipantsScreen(
    eventId: Int,
    viewModel: ParticipantViewModel,
    navController: NavController
) {
    var participants by remember { mutableStateOf<List<ParticipantResponse>>(emptyList()) }
    var emailToInvite by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getParticipantsByEventId(eventId, { participants = it }, { errorMessage = it })
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Gerir Participantes",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TextField(
                value = emailToInvite,
                onValueChange = { emailToInvite = it },
                label = { Text("Email do participante") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.inviteParticipant(eventId, emailToInvite, {
                        participants = participants + it
                        emailToInvite = ""
                        errorMessage = ""
                    }, { error ->
                        errorMessage = error
                    })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Convidar Participante")
            }

            if (errorMessage.isNotBlank()) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Participantes:", style = MaterialTheme.typography.titleMedium)

            LazyColumn {
                items(participants) { participant ->
                    ParticipantItem(participant = participant, viewModel = viewModel, onUpdate = {
                        viewModel.getParticipantsByEventId(eventId, { updatedParticipants ->
                            participants = updatedParticipants
                        }, { errorMessage = it })
                    })
                }
            }
        }
    }
}

@Composable
fun ParticipantItem(
    participant: ParticipantResponse,
    viewModel: ParticipantViewModel,
    onUpdate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Coluna com Nome e Status
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = participant.userName ?: "N/A",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = participant.status ?: "N/A",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Botão de ações com Dropdown
            Box {
                Button(onClick = { expanded = true }) {
                    Text("Ações")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Confirmar") },
                        onClick = {
                            viewModel.updateParticipantStatus(participant.id, "confirmed", {
                                onUpdate() // Atualiza a lista de participantes
                            }, {})
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Recusar") },
                        onClick = {
                            viewModel.updateParticipantStatus(participant.id, "declined", {
                                onUpdate() // Atualiza a lista de participantes
                            }, {})
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

