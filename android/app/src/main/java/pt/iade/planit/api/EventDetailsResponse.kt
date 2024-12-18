package pt.iade.planit.api

data class EventDetailsResponse(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val photoUrl: String?,
    val userName: String,
    val location: LocationResponse?,
    val participants: List<ParticipantResponse>
)

data class LocationResponse(
    val latitude: Double,
    val longitude: Double,
    val address: String
)

data class ParticipantResponse(
    val userId: Int,
    val userName: String,
    val status: String
)
