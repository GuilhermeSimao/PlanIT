package pt.iade.planit.api

data class Event(
    val id: Int? = null,
    val userId: Int,
    val title: String,
    val description: String,
    val date: String,
    val photoUrl: String?,
    val latitude: Double,
    val longitude: Double
)
