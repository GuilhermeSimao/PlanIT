// LoginViewModel.kt (ou em sua Activity)
package pt.iade.planit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.iade.planit.api.Event
import pt.iade.planit.api.RetrofitInstance
import pt.iade.planit.api.User
import pt.iade.planit.api.UserCredentials

class LoginViewModel : ViewModel() {

    // Função para login
    fun loginUser(email: String, password: String, onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.login(UserCredentials(email, password))
                Log.d("Login", "Usuário logado com sucesso: ${response}")
                withContext(Dispatchers.Main) {
                    onSuccess(response.id)
                }
            } catch (e: Exception) {
                Log.e("Login", "Erro ao fazer login", e)
            }
        }
    }

    // Função para registrar o usuário
    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = User(name = name, email = email, password = password)
                val response = RetrofitInstance.api.register(user)
                Log.d("Register", "Usuário registrado: $response")
            } catch (e: Exception) {
                Log.e("Register", "Erro ao registrar usuário", e)
            }
        }
    }
    fun getUserEvents(userId: Int, onResult: (List<Event>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val events = RetrofitInstance.api.getUserEvents(userId)
                withContext(Dispatchers.Main) {
                    onResult(events)
                }
            } catch (e: Exception) {
                Log.e("Events", "Error fetching user events", e)
                withContext(Dispatchers.Main) {
                    onResult(emptyList()) // Return an empty list on error
                }
            }
        }
    }

    fun createEvent(
        userId: Int,
        title: String,
        description: String,
        date: String,
        photoUrl: String?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val event = Event(0, title, description, date, photoUrl, userId, null)
                RetrofitInstance.api.createEvent(event)
                withContext(Dispatchers.Main) {
                    onSuccess()
                }
            } catch (e: Exception) {
                Log.e("CreateEvent", "Error creating event", e)
            }
        }
    }
}

