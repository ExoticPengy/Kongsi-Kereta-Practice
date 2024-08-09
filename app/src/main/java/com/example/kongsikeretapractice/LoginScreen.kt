package com.example.kongsikeretapractice

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Login(
    authViewModel: AuthViewModel,
    loginViewModel: LoginViewModel = viewModel(),
    goRegister: () -> Unit,
    loginDriver: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState.authState) {
            "Authenticated" -> {
                Toast.makeText(context, "Successfully Authenticated!", Toast.LENGTH_SHORT).show()
                loginDriver()
            }
            "Unsuccessful" -> {
                Toast.makeText(context, authState.error, Toast.LENGTH_SHORT).show()
            }
            "Empty" -> {
                Toast.makeText(context, authState.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Kongsi Kereta App",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )


            OutlinedTextField(
                value = loginViewModel.ic,
                onValueChange = { loginViewModel.ic = it },
                label = { Text("IC") }
            )

            OutlinedTextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.password = it },
                label = { Text("Password") }
            )

            Button(
                onClick = {
                    loginViewModel.getCredentials()
                    authViewModel.logIn(loginViewModel.ic, loginViewModel.email, loginViewModel.password)
                          },
                colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
            ) {
                Text("Log In")
            }

            Button(
                onClick = {
                    goRegister()
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text("Don't have an account? Register here!", color = Color.Blue)
            }
        }
    }
}