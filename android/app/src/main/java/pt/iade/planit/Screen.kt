package pt.iade.planit

sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object Login : Screen("login")
    object Register : Screen("register")
    object CreateEvent : Screen("create_event")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}
