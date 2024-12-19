package pt.iade.planit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.iade.planit.api.ParticipantResponse
import pt.iade.planit.api.RetrofitInstance

class ParticipantViewModel : ViewModel() {

    fun inviteParticipant(eventId: Int, userEmail: String, onSuccess: (ParticipantResponse) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.inviteParticipant(eventId, userEmail)
                withContext(Dispatchers.Main) { onSuccess(response) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e.message ?: "Erro desconhecido") }
            }
        }
    }

    fun updateParticipantStatus(participantId: Int?, status: String?, onSuccess: (ParticipantResponse) -> Unit, onError: (String) -> Unit) {
        if (participantId == null || status.isNullOrBlank()) {
            onError("Dados inválidos fornecidos.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.updateParticipantStatus(participantId, status)
                withContext(Dispatchers.Main) { onSuccess(response) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e.message ?: "Erro desconhecido") }
            }
        }
    }


    fun getParticipantsByEventId(eventId: Int, onSuccess: (List<ParticipantResponse>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getParticipantsByEventId(eventId)
                withContext(Dispatchers.Main) { onSuccess(response) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e.message ?: "Erro desconhecido") }
            }
        }
    }

    fun getPendingInvites(userId: Int, onSuccess: (List<ParticipantResponse>) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("Fetching pending invites for userId: $userId") // Debug
                val response = RetrofitInstance.api.getPendingInvites(userId)
                withContext(Dispatchers.Main) { onSuccess(response) }
            } catch (e: Exception) {
                println("Error fetching invites: ${e.message}") // Debug
                withContext(Dispatchers.Main) { onError(e.message ?: "Erro desconhecido") }
            }
        }
    }


}
