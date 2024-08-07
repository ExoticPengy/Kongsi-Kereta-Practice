package com.example.kongsikeretapractice

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.kongsikeretapractice.ui.theme.KongsiKeretaPracticeTheme
import kotlinx.coroutines.launch

@Composable
fun Register(
    registerViewModel: RegisterViewModel = viewModel(),
    confirmAccount: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.size(60.dp))

                Text(text = "Register as Driver")

                Spacer(Modifier.size(20.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                        progress = registerViewModel.currentProgress,
                        color = toColor("#a3bcdc"),
                        trackColor = Color.LightGray,
                        modifier = Modifier
                            .height(10.dp)
                            .width(180.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(185.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(25.dp)
                                .background(
                                    color = registerViewModel.box1,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = "1",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(25.dp)
                                .background(
                                    color = registerViewModel.box2,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = "2",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(25.dp)
                                .background(
                                    color = registerViewModel.box3,
                                    shape = RoundedCornerShape(50)
                                )
                        ) {
                            Text(
                                text = "3",
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            AnimatedContent(
                targetState = registerViewModel.step,
                transitionSpec = {
                    if (!registerViewModel.back) {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(500),
                                    targetOffsetX = { fullWidth -> -fullWidth }
                                )
                    } else {
                        slideInHorizontally(
                            animationSpec = tween(500),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) togetherWith
                                slideOutHorizontally(
                                    animationSpec = tween(500),
                                    targetOffsetX = { fullWidth -> fullWidth }
                                )
                    }
                }) {
                when (it) {
                    1 -> {
                        Step1(registerViewModel)
                    }

                    2 -> {
                        Step2(registerViewModel)
                    }

                    else -> {
                        Step3(registerViewModel)
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(Modifier.size(1.dp))

                if (registerViewModel.step != 1) {
                    Button(
                        onClick = {
                            registerViewModel.back = true
                            if (registerViewModel.step == 2) {
                                scope.launch {
                                    registerViewModel.barAnimation(-50, 0, 2)
                                }
                                registerViewModel.step = 1
                            } else {
                                scope.launch {
                                    registerViewModel.barAnimation(-100, -50, 3)
                                }
                                registerViewModel.step = 2
                            }
                        },
                        colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
                        enabled = registerViewModel.buttonEnabled
                    ) {
                        Text(text = "Previous")
                    }
                }

                Button(
                    onClick = {
                        registerViewModel.back = false
                        when (registerViewModel.step) {
                            1 -> {
                                scope.launch {
                                    registerViewModel.barAnimation(0, 50, 2)
                                }
                                registerViewModel.step = 2
                            }

                            2 -> {
                                scope.launch {
                                    registerViewModel.barAnimation(50, 100, 3)
                                }
                                registerViewModel.step = 3
                            }

                            else -> {
                                registerViewModel.uploadProfile(context)
                                confirmAccount()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
                    enabled = registerViewModel.buttonEnabled
                ) {
                    Text(text = "Next")
                }

                Spacer(Modifier.size(1.dp))
            }
            Spacer(Modifier.size(50.dp))
        }
    }
}

@Composable
fun Step1(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()
    val DriverInfo = uiState.driverInfo

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        OutlinedTextField(
            value = DriverInfo.name,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(name = it)
                )
            },
            label = { Text(text = "Name") }
        )

        OutlinedTextField(
            value = DriverInfo.ic,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(ic = it)
                )
            },
            label = { Text(text = "IC") }
        )

        OutlinedTextField(
            value = DriverInfo.gender,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(gender = it)
                )
            },
            label = { Text(text = "Gender") }
        )

        OutlinedTextField(
            value = DriverInfo.phone,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(phone = it)
                )
            },
            label = { Text(text = "Phone") }
        )

        OutlinedTextField(
            value = DriverInfo.email,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(email = it)
                )
            },
            label = { Text(text = "Email") }
        )

        OutlinedTextField(
            value = DriverInfo.address,
            onValueChange = {
                registerViewModel.updateUiState(
                    DriverInfo.copy(address = it)
                )
            },
            label = { Text(text = "Address") }
        )
    }
}

@Composable
fun Step2(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()
    val driverInfo = uiState.driverInfo

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        OutlinedTextField(
            value = driverInfo.model,
            onValueChange = {
                registerViewModel.updateUiState(
                    driverInfo.copy(model = it)
                )
            },
            label = { Text(text = "Car Model") }
        )

        OutlinedTextField(
            value = driverInfo.capacity,
            onValueChange = {
                registerViewModel.updateUiState(
                    driverInfo.copy(capacity = it)
                )
            },
            label = { Text(text = "Sitting Capacity") }
        )

        OutlinedTextField(
            value = driverInfo.special,
            onValueChange = {
                registerViewModel.updateUiState(
                    driverInfo.copy(special = it)
                )
            },
            label = { Text(text = "Special Features") }
        )
    }
}

@Composable
fun Step3(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            registerViewModel.selectedImageUri = it
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

        Box(
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
                .clip(RoundedCornerShape(50))
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(50))
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(50)),
                model = registerViewModel.selectedImageUri,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Button(modifier = Modifier
            .width(250.dp)
            .height(56.dp),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
            enabled = registerViewModel.buttonEnabled,
            onClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(
                text = "Pick an Image",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}