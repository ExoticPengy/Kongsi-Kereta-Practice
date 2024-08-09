package com.example.kongsikeretapractice

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
fun Register(
    registerViewModel: RegisterViewModel,
    confirmAccount: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = registerViewModel.registerAsDriver,
            transitionSpec = {
                fadeIn(spring(Spring.DampingRatioHighBouncy)) togetherWith fadeOut()
            }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                if (it) {
                    DriverRegister(registerViewModel = registerViewModel, confirmAccount)
                } else {
                    StudentRegister(registerViewModel = registerViewModel, confirmAccount)
                }
            }
        }
    }
}

@Composable
fun DriverRegister(
    registerViewModel: RegisterViewModel,
    confirmAccount: () -> Unit
) {
    val context = LocalContext.current
    val progressState by animateFloatAsState(
        targetValue = (registerViewModel.step - 1) / 2.toFloat(),
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(60.dp))

        Text(text = "Register as Driver")
        Button(
            onClick = {
                registerViewModel.registerAsDriver = !registerViewModel.registerAsDriver
                registerViewModel.changeStep(1)
                registerViewModel.updateUiState(RegisterViewModel.UserInfo())
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text("Want to register as a rider instead? Tap here!", color = Color.Blue)
        }

        Spacer(Modifier.size(20.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = progressState,
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
                    if (registerViewModel.step == 2) {
                        registerViewModel.changeStep(1)
                    } else {
                        registerViewModel.changeStep(2)
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
                when (registerViewModel.step) {
                    1 -> {
                        registerViewModel.uploadProfile(context)
                        registerViewModel.changeStep(2)
                    }
                    2 -> {
                        registerViewModel.changeStep(3)
                    }
                    else -> {
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

@Composable
fun StudentRegister(
    registerViewModel: RegisterViewModel,
    confirmAccount: () -> Unit
) {
    val context = LocalContext.current
    val progressState by animateFloatAsState(
        targetValue = (registerViewModel.step - 1).toFloat()
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(60.dp))

        Text(text = "Register as Rider")
        Button(
            onClick = {
                registerViewModel.registerAsDriver = !registerViewModel.registerAsDriver
                registerViewModel.changeStep(1)
                registerViewModel.updateUiState(RegisterViewModel.UserInfo())
            },
            colors = ButtonDefaults.buttonColors(Color.Transparent)
        ) {
            Text("Want to register as a driver instead? Tap here!", color = Color.Blue)
        }

        Spacer(Modifier.size(20.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            LinearProgressIndicator(
                progress = progressState,
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

                else -> {
                    Step2(registerViewModel)
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
                        if (registerViewModel.step == 2) {
                            registerViewModel.changeStep(1)
                        } else {
                            registerViewModel.changeStep(2)
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
                    when (registerViewModel.step) {
                        1 -> {
                            registerViewModel.uploadProfile(context)
                            registerViewModel.changeStep(2)
                        }
                        else -> {
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
    }
}

@Composable
fun Step1(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo
    val vehicleInfo = uiState.vehicleInfo
    val context = LocalContext.current

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
            .height(400.dp)
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
    }
}

@Composable
fun Step2(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo
    val vehicleInfo = uiState.vehicleInfo

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
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
    }
}

@Composable
fun Step3(registerViewModel: RegisterViewModel) {
    val uiState by registerViewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo
    val vehicleInfo = uiState.vehicleInfo

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {

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

@Composable
fun Confirm(
    authViewModel: AuthViewModel,
    registerViewModel: RegisterViewModel,
    accountCreated: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()
    val userInfo = uiState.userInfo
    val vehicleInfo = uiState.vehicleInfo
    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState.authState) {
            "Success" -> {
                if (registerViewModel.registerAsDriver) {
                    registerViewModel.updateUiState(userInfo.copy(uid = authViewModel.uid), vehicleInfo)
                    registerViewModel.saveDriver()
                }
                else {
                    registerViewModel.updateUiState(userInfo.copy(uid = authViewModel.uid))
                    registerViewModel.saveRider()
                }
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
                    text = "Name: ${userInfo.name}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                Text(
                    text = "IC: ${userInfo.ic}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                Text(
                    text = "Gender: ${userInfo.gender}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                Text(
                    text = "Phone: ${userInfo.phone}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                Text(
                    text = "Email: ${userInfo.email}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                Text(
                    text = "Address: ${userInfo.address}",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(330.dp)
                )
                if (registerViewModel.registerAsDriver) {
                    Text(
                        text = "Car Details",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Car Model: ${vehicleInfo.model}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(330.dp)
                    )
                    Text(
                        text = "Sitting Capacity: ${vehicleInfo.capacity}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(330.dp)
                    )
                    Text(
                        text = "Special Features: ${vehicleInfo.special}",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.width(330.dp)
                    )
                }
                Spacer(Modifier.size(1.dp))
            }

            Button(
                colors = ButtonDefaults.buttonColors(toColor("#658fc4")),
                onClick = {
                    authViewModel.signUp(uiState.userInfo.email, registerViewModel.password)
                }
            ) {
                Text(text = "Confirm")
            }
            Spacer(Modifier.size(1.dp))
        }
    }
}