package com.example.kongsikeretapractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kongsikeretapractice.ui.theme.KongsiKeretaPracticeTheme

enum class KongsiKereta() {
    Login,
    Register,
    Confirm,
    Driver,
    Rider
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            KongsiKeretaPracticeTheme {
                AppNavigation(authViewModel)
            }
        }
    }
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
                loginDriver = { navController.navigate(KongsiKereta.Driver.name) }
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
                ) }
            )
        }
    }
}

