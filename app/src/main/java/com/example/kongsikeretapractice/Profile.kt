package com.example.kongsikeretapractice

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Profile(
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    backClick: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo
    val vehicleInfo = uiState.vehicleInfo

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            registerViewModel.selectedImageUri = it
        }
    )

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(50))
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(50))
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(50)),
                    model = registerViewModel.selectedImageUri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .offset(x = 50.dp, y = 50.dp)
                        .background(Color.Black, RoundedCornerShape(50))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                }
            }

            Text("Account Details")

            OutlinedTextField(
                value = userInfo.ic,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(ic = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "IC") }
            )

            OutlinedTextField(
                value = userInfo.email,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(email = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "Email") }
            )

            OutlinedTextField(
                value = registerViewModel.password,
                onValueChange = {
                    registerViewModel.password = it
                },
                label = { Text(text = "Password") }
            )

            Text("Personal Details")

            OutlinedTextField(
                value = userInfo.name,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(name = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "Name") }
            )

            OutlinedTextField(
                value = userInfo.gender,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(gender = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "Gender") }
            )

            OutlinedTextField(
                value = userInfo.phone,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(phone = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "Phone") }
            )

            OutlinedTextField(
                value = userInfo.address,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(address = it),
                        vehicleInfo.copy()
                    )
                },
                label = { Text(text = "Address") }
            )

            Text("Vehicle Details")

            OutlinedTextField(
                value = vehicleInfo.model,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(),
                        vehicleInfo.copy(model = it)
                    )
                },
                label = { Text(text = "Car Model") }
            )

            OutlinedTextField(
                value = vehicleInfo.capacity,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(),
                        vehicleInfo.copy(capacity = it)
                    )
                },
                label = { Text(text = "Sitting Capacity") }
            )

            OutlinedTextField(
                value = vehicleInfo.special,
                onValueChange = {
                    registerViewModel.updateUiState(
                        userInfo.copy(),
                        vehicleInfo.copy(special = it)
                    )
                },
                label = { Text(text = "Special Features") }
            )
        }
    }
}