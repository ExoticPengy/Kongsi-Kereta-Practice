package com.example.kongsikeretapractice

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Login(
    authViewModel: AuthViewModel,
    loginViewModel: LoginViewModel = viewModel(),
    goRegister: () -> Unit,
    loginDriver: () -> Unit,
    loginRider: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState.authState) {
            "Authenticated" -> {
                loginViewModel.loading = false
                Toast.makeText(context, "Successfully Authenticated!", Toast.LENGTH_SHORT).show()
                if (loginViewModel.isDriver) {
                    loginDriver()
                }
                else {
                    loginRider()
                }
            }
            "Loading" -> {
                loginViewModel.loading = true
            }
            "Unsuccessful" -> {
                loginViewModel.loading = false
                Toast.makeText(context, authState.error, Toast.LENGTH_SHORT).show()
            }
            "Empty" -> {
                loginViewModel.loading = false
                Toast.makeText(context, authState.error, Toast.LENGTH_SHORT).show()
                authViewModel.updateAuthState("")
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
            if (loginViewModel.loading) {
                LoadingDialog()
            }

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
                label = { Text("IC no.") }
            )

            OutlinedTextField(
                value = loginViewModel.password,
                onValueChange = { loginViewModel.password = it },
                label = { Text("Password") },
                visualTransformation = if (loginViewModel.showPassword) VisualTransformation.None
                else PasswordVisualTransformation(),
                trailingIcon = { Icon(
                    if (loginViewModel.showPassword) Icons.Default.Visibility
                    else Icons.Default.VisibilityOff,
                    null,
                    modifier = Modifier.clickable { loginViewModel.showPassword = !loginViewModel.showPassword }
                ) }
            )

            Button(
                onClick = {
                    loginViewModel.getCredentials()
                    authViewModel.logIn(loginViewModel.ic, loginViewModel.email, loginViewModel.password)
                    loginViewModel.reset()
                          },
                colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
                enabled = (authState.authState != "Loading")
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