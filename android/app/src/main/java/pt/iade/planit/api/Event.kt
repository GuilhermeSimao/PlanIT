package pt.iade.planit.api

data class Event(
    val id: Int? = null,
    val userId: Int,
    val title: String,
    val description: String,
    val date: String,
    val photoUrl: String? = null,
    val latitude: Double,
    val longitude: Double,
    val address: String
)
