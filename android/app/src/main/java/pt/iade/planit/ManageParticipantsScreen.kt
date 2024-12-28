package pt.iade.planit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
            TopAppBar(
                title = { Text("Gerir Participantes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
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
                        errorMessage = "" // Limpa a mensagem de erro no sucesso
                    }, { error ->
                        errorMessage = error // Exibe a mensagem de erro
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
                        // Atualiza a lista após mudança de status
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = participant.userName ?: "N/A", style = MaterialTheme.typography.bodyLarge)
        Text(text = participant.status ?: "N/A", style = MaterialTheme.typography.bodySmall)
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