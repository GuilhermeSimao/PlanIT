package pt.iade.planit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.iade.planit.api.ParticipantResponse
import pt.iade.planit.viewmodel.ParticipantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingInvitesScreen(userId: Int, viewModel: ParticipantViewModel, navController: NavController) {
    var invites by remember { mutableStateOf<List<ParticipantResponse>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.getPendingInvites(userId, { invites = it }, { errorMessage = it })
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                title = "Gestão de Convites",
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (invites.isEmpty() && errorMessage.isBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sem convites pendentes.", textAlign = TextAlign.Center)
                }
            } else if (errorMessage.isNotBlank()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(invites) { invite ->
                        InviteCard(
                            invite = invite,
                            onCardClick = { eventId ->
                                navController.navigate(Screen.DetailScreen.withArgs(eventId.toString()))
                            },
                            onAccept = {
                                viewModel.updateParticipantStatus(invite.id, "confirmed", {
                                    invites = invites.filter { it.id != invite.id }
                                }, { error -> errorMessage = error })
                            },
                            onDecline = {
                                viewModel.updateParticipantStatus(invite.id, "declined", {
                                    invites = invites.filter { it.id != invite.id }
                                }, { error -> errorMessage = error })
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InviteCard(
    invite: ParticipantResponse,
    onCardClick: (Int) -> Unit,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick(invite.eventId) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = invite.eventTitle,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Status: ${invite.status}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onAccept,
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text("Aceitar")
                }
                Button(
                    onClick = onDecline,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text("Recusar")
                }
            }
        }
    }
}
