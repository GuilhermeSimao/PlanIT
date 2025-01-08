package pt.iade.planit

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object Login : Screen("login")
    object Register : Screen("register")
    object CreateEvent : Screen("create_event")
    object DetailScreen : Screen("detail_screen/{eventId}/{currentUserId}") {
        fun withArgs(eventId: String, currentUserId: String): String =
            "detail_screen/$eventId/$currentUserId"
    }
    object ManageParticipants : Screen("manage_participants/{eventId}") {
        fun withArgs(eventId: String): String = "manage_participants/$eventId"
    }
    object PendingInvites : Screen("pending_invites/{userId}") {
        fun withArgs(userId: String): String {
            val route = "pending_invites/$userId"
            println("Defining route for PendingInvites: $route")
            return route
        }
    }
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
