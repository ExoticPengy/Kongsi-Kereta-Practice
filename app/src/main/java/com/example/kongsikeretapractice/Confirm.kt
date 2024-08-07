package com.example.kongsikeretapractice

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.storage.storage

@Composable
fun Confirm(
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel = viewModel(),
    accountCreated: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val driverInfo = uiState.driverInfo
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState.authState) {
            "Success" -> {
                registerViewModel.saveDriver(uiState.driverInfo)
                accountCreated()
            }
            "Failed" -> {
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
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(Modifier.size(1.dp))
            Text("Confirmation of Profile Details")

            AnimatedContent(
                targetState = registerViewModel.passwordView,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                }
                ) {confirm ->
                if(confirm) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .width(350.dp)
                            .height(200.dp)
                    ) {
                        Text("One last thing! Enter your password:")
                        Spacer(Modifier.size(1.dp))

                        OutlinedTextField(
                            value = registerViewModel.password,
                            onValueChange = {
                                registerViewModel.password = it
                            },
                            label = { Text(text = "Password") }
                        )
                        OutlinedTextField(
                            value = registerViewModel.confirmPassword,
                            onValueChange = {
                                registerViewModel.confirmPassword = it
                            },
                            label = { Text(text = "Confirm Password") }
                        )
                    }
                }
                else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .width(350.dp)
                            .height(500.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(10))
                            .verticalScroll(rememberScrollState())
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .width(80.dp)
                                .height(80.dp)
                                .clip(RoundedCornerShape(50)),
                            model = registerViewModel.selectedImageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = "Personal Details",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Name: ${driverInfo.name}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "IC: ${driverInfo.ic}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Gender: ${driverInfo.gender}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Phone: ${driverInfo.phone}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Email: ${driverInfo.email}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Address: ${driverInfo.address}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Car Details",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = "Car Model: ${driverInfo.model}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Sitting Capacity: ${driverInfo.capacity}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Text(
                            text = "Special Features: ${driverInfo.special}",
                            textAlign = TextAlign.Start,
                            modifier = Modifier.width(330.dp)
                        )
                        Spacer(Modifier.size(1.dp))
                    }
                }
            }

            Button(
                colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
                onClick = {
                    if (registerViewModel.passwordView) {
                        if (uiState.driverInfo.imageUrl != "") {
                                authViewModel.signUp(uiState.driverInfo.email, registerViewModel.password)
                        }
                        else {
                            Toast.makeText(
                                context,
                                "Failed to save image uri.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    else {
                        registerViewModel.passwordView = true
                    }
                }
            ) {
                Text(text = "Confirm")
            }
            Spacer(Modifier.size(1.dp))
        }
    }
}