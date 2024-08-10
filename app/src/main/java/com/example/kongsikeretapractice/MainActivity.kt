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



