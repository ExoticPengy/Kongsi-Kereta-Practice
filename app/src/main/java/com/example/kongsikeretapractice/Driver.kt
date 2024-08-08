package com.example.kongsikeretapractice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Driver(
    authViewModel: AuthViewModel,
    rideViewModel: RideViewModel = viewModel(),
    backLogin: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val uiState by rideViewModel.uiState.collectAsState()
    val rides: List<RideViewModel.RideDetails> = uiState.rides

    LaunchedEffect(authState) {
        if (authState.authState == "Authenticated") {
            if (rideViewModel.userIc == "") {
                rideViewModel.userIc = authViewModel.userIc
            }
            rideViewModel.getRides()
        }
        else if (authState.authState == "Unauthenticated") {
            backLogin()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                Spacer(Modifier.fillMaxWidth())
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = { authViewModel.logOut() },
                        colors = IconButtonDefaults.iconButtonColors(Color.Red),
                        modifier = Modifier
                            .background(
                                Color.Red,
                                shape = RoundedCornerShape(20)
                            )
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }

                    Text("Kongsi Kereta Ride")

                    IconButton(
                        onClick = { /*TODO*/ },
                        colors = IconButtonDefaults.iconButtonColors(Color.Black),
                        modifier = Modifier
                            .background(
                                Color.Black,
                                shape = RoundedCornerShape(50)
                            )
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
                Divider(
                    thickness = 1.dp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(500.dp)
            ) {
                items(rides) { ride ->
                    Spacer(Modifier.height(25.dp))
                    Card() {
                        Column() {
                            var date by remember { mutableStateOf(ride.date) }
                            var time by remember { mutableStateOf(ride.time) }
                            var destination by remember { mutableStateOf(ride.destination) }
                            var origin by remember { mutableStateOf(ride.origin) }
                            var fare by remember { mutableStateOf(ride.fare) }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = date,
                                    onValueChange = {
                                        date = it
                                        rideViewModel.newRideDetails.date = it
                                    },
                                    label = { Text("Date") },
                                    enabled = false
                                )
                                Spacer(Modifier.width(3.dp))
                                if (rideViewModel.currentEdit == ride.rideId) {
                                    IconButton(
                                        onClick = { rideViewModel.datePicker = true },
                                    ) {
                                        Icon(Icons.Default.DateRange, contentDescription = null)
                                    }
                                }
                                if (rideViewModel.datePicker && rideViewModel.currentEdit == ride.rideId) {
                                    DatePickerModal(
                                        onDateSelected = {
                                            date = formatDate(it ?: 0)
                                            rideViewModel.newRideDetails.date = formatDate(it ?: 0)
                                                         },
                                        onDismiss = { rideViewModel.datePicker = false }
                                    )
                                }
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = time,
                                    onValueChange = {
                                        time = it
                                        rideViewModel.newRideDetails.time = it
                                    },
                                    label = { Text("Time") },
                                    enabled = false
                                )
                                Spacer(Modifier.width(3.dp))
                                if (rideViewModel.currentEdit == ride.rideId) {
                                    IconButton(
                                        onClick = { rideViewModel.timePicker = true },
                                    ) {
                                        Icon(Icons.Default.Create, contentDescription = null)
                                    }
                                }
                                if (rideViewModel.timePicker && rideViewModel.currentEdit == ride.rideId) {
                                    TimeDial(
                                        onConfirm = {
                                            time = it
                                            rideViewModel.newRideDetails.time = it
                                        },
                                        onDismiss = { rideViewModel.timePicker = false }
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = destination,
                                onValueChange = {
                                    destination = it
                                    rideViewModel.newRideDetails.destination = it
                                },
                                label = { Text("Destination") },
                                enabled = rideViewModel.currentEdit == ride.rideId
                            )
                            OutlinedTextField(
                                value = origin,
                                onValueChange = {
                                    origin = it
                                    rideViewModel.newRideDetails.origin = it
                                },
                                label = { Text("Origin") },
                                enabled = rideViewModel.currentEdit == ride.rideId
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                OutlinedTextField(
                                    value = toRm(fare),
                                    onValueChange = {
                                        fare = toAmount(it)
                                        rideViewModel.newRideDetails.fare = toAmount(it)
                                    },
                                    label = { Text("Fare") },
                                    enabled = rideViewModel.currentEdit == ride.rideId
                                )
                                Spacer(Modifier.width(10.dp))
                                IconButton(
                                    onClick = {
                                        if (rideViewModel.currentEdit == -1) {
                                            rideViewModel.newRideDetails = ride
                                            rideViewModel.currentEdit = ride.rideId
                                        } else {
                                            rideViewModel.currentEdit = -1
                                            rideViewModel.newRideDetails.driverIc =
                                                rideViewModel.userIc
                                            rideViewModel.newRideDetails.rideId = ride.rideId
                                            rideViewModel.updateRide()
                                        }

                                        if (rideViewModel.userIc == "") {
                                            rideViewModel.userIc = authViewModel.userIc
                                        }
                                    },
                                    colors = IconButtonDefaults.iconButtonColors(Color.Black),
                                    modifier = Modifier
                                        .background(
                                            Color.Black,
                                            shape = RoundedCornerShape(50)
                                        )
                                        .size(35.dp)
                                ) {
                                    Icon(
                                        if (rideViewModel.currentEdit == ride.rideId) {
                                            Icons.Default.Check
                                        } else {
                                            Icons.Default.Edit
                                        },
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                                Spacer(Modifier.width(10.dp))
                            }
                        }
                    }
                    Spacer(Modifier.height(25.dp))
                }
            }

            Spacer(Modifier.height(25.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .background(
                        Color.Black,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable {
                        if (rideViewModel.userIc == "") {
                            rideViewModel.userIc = authViewModel.userIc
                        }
                        rideViewModel.newRide()
                    }
            ) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(25.dp)
                        .clickable { }
                )
                Text(
                    text = "Add new ride",
                    color = Color.White
                )
            }
        }
    }
}