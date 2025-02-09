package pt.iade.planit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.iade.planit.api.Event
import pt.iade.planit.api.EventDetailsResponse
import pt.iade.planit.api.RetrofitInstance

class EventDetailsViewModel : ViewModel() {

    var eventDetails: EventDetailsResponse? = null
        private set

    fun fetchEventDetails(eventId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                eventDetails = RetrofitInstance.api.getEventDetails(eventId)
                withContext(Dispatchers.Main) { onSuccess() }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { onError(e.message ?: "Erro ao buscar os detalhes do evento") }
            }
        }
    }

    fun deleteEvent(eventId: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Chamada para a API usando RetrofitInstance
                RetrofitInstance.api.deleteEvent(eventId)
                // Volta para a thread principal para executar a callback de sucesso
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                // Volta para a thread principal para executar a callback de erro
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "Erro ao excluir o evento")
                }
            }
        }
    }

    fun updateEvent(
        eventId: Int,
        updatedEvent: Event,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                RetrofitInstance.api.updateEvent(eventId, updatedEvent)

                eventDetails = eventDetails?.copy(
                    latitude = updatedEvent.latitude,
                    longitude = updatedEvent.longitude,
                    address = updatedEvent.address
                )

                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError(e.message ?: "Erro ao atualizar o evento")
                }
            }
        }
    }




}
