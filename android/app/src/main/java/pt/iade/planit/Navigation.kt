package pt.iade.planit

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screen.Login.route){
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

    }

}