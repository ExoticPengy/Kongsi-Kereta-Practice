package com.example.kongsikeretapractice

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class KongsiKereta() {
    Login,
    Register,
    Confirm,
    Driver,
    Rider,
    Profile
}

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController: NavHostController = rememberNavController()
    val registerViewModel: RegisterViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = KongsiKereta.Login.name
    ) {
        composable(route = KongsiKereta.Login.name) {
            Login(
                authViewModel,
                goRegister = { navController.navigate(KongsiKereta.Register.name) },
                loginDriver = { navController.navigate(KongsiKereta.Driver.name) },
                loginRider = { navController.navigate(KongsiKereta.Rider.name) }
            )
        }
        composable(route = KongsiKereta.Register.name) {
            Register(
                registerViewModel,
                confirmAccount = { navController.navigate(KongsiKereta.Confirm.name) }
            )
        }
        composable(route = KongsiKereta.Confirm.name) {
            Confirm(
                authViewModel,
                registerViewModel,
                accountCreated = {
                    navController.popBackStack(KongsiKereta.Login.name, false)
                }
            )
        }
        composable(route = KongsiKereta.Driver.name) {
            Driver(
                authViewModel,
                backLogin = { navController.popBackStack(
                    KongsiKereta.Login.name,
                    false
                ) },
                profileClick = {
                    navController.navigate(KongsiKereta.Profile.name)
                }
            )
        }
        composable(route = KongsiKereta.Rider.name) {
            Rider(
                authViewModel,
                backLogin = { navController.popBackStack(
                    KongsiKereta.Login.name,
                    false
                ) },
                profileClick = {
                    navController.navigate(KongsiKereta.Profile.name)
                }
            )
        }
        composable(route = KongsiKereta.Profile.name) {
            Profile(
                authViewModel,
                backClick = { navController.popBackStack() },
                registerViewModel = registerViewModel
            )
        }
    }
}
