package pt.iade.planit.api

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val photoUrl: String?,
    val userId: Int,
    val locationId: Int?
)
