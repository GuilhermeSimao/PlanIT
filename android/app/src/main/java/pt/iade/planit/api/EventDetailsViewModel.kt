package pt.iade.planit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
}
