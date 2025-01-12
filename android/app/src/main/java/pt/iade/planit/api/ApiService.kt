package pt.iade.planit.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/user/login")
    suspend fun login(@Body credentials: UserCredentials): UserResponse

    @POST("/user/register")
    suspend fun register(@Body user: User): UserResponse

    @GET("/event/user/{userId}")
    suspend fun getUserEvents(@Path("userId") userId: Int): List<Event>

    @POST("/event/add")
    suspend fun createEvent(@Body event: Event): Event

    @GET("/event/{id}")
    suspend fun getEventDetails(@Path("id") eventId: Int): EventDetailsResponse

    @POST("/participant/event/{eventId}/invite")
    suspend fun inviteParticipant(
        @Path("eventId") eventId: Int,
        @Query("userEmail") userEmail: String
    ): ParticipantResponse

    @PATCH("/participant/{participantId}/status")
    suspend fun updateParticipantStatus(
        @Path("participantId") participantId: Int,
        @Query("status") status: String
    ): ParticipantResponse

    @GET("/participant/event/{eventId}")
    suspend fun getParticipantsByEventId(
        @Path("eventId") eventId: Int
    ): List<ParticipantResponse>

    @GET("participant/user/{userId}/pending")
    suspend fun getPendingInvites(@Path("userId") userId: Int): List<ParticipantResponse>

    @GET("/event/participating/confirmed/{userId}")
    suspend fun getConfirmedEvents(@Path("userId") userId: Int): List<Event>

    @DELETE("/event/delete/{eventId}")
    suspend fun deleteEvent(@Path("eventId") eventId: Int)

    @PUT("/event/update/{eventId}")
    suspend fun updateEvent(
        @Path("eventId") eventId: Int,
        @Body event: Event
    ): Response<Unit>


}
