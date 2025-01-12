package pt.iade.planit

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel
import pt.iade.planit.viewmodel.ParticipantViewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val participantViewModel: ParticipantViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginScreen(loginViewModel = loginViewModel, navController = navController)
        }

        composable(
            route = Screen.MainScreen.route + "/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entry ->
            val id = entry.arguments?.getInt("id")
            MainScreen(
                id = id,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                navController = navController,
                registerViewModel = loginViewModel
            )
        }

        composable(route = Screen.CreateEvent.route + "/{userId}") {
            val userId = it.arguments?.getString("userId")?.toInt() ?: -1
            CreateEventScreen(
                navController = navController,
                loginViewModel = loginViewModel,
                userId = userId
            )
        }

        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("currentUserId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            val currentUserId = backStackEntry.arguments?.getInt("currentUserId") ?: 0
            DetailScreen(
                eventId = eventId,
                currentUserId = currentUserId,
                viewModel = viewModel(),
                navController = navController
            )
        }

        composable(
            route = Screen.ManageParticipants.route,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            ManageParticipantsScreen(
                eventId = eventId,
                viewModel = participantViewModel,
                navController = navController
            )
        }

        composable(
            route = Screen.PendingInvites.route,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val currentUserId = backStackEntry.arguments?.getInt("userId") ?: 0
            PendingInvitesScreen(
                userId = userId,
                currentUserId = currentUserId,
                viewModel = participantViewModel,
                navController = navController
            )
        }

        composable(
            route = "confirmed_events/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            ConfirmedEventsScreen(
                userId = userId,
                loginViewModel = loginViewModel,
                navController = navController
            )
        }

        composable(
            route = "edit_event/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            EditEventScreen(eventId = eventId, viewModel = viewModel(), navController = navController)
        }


    }
}