package pt.iade.planit.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

}
