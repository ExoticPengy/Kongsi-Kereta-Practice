package com.example.kongsikeretapractice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun Rider(
    authViewModel: AuthViewModel,
    rideViewModel: RideViewModel = viewModel(),
    backLogin: () -> Unit,
    profileClick: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val uiState by rideViewModel.uiState.collectAsState()
    val rides = uiState.rides
    val trips = uiState.trips

    LaunchedEffect(authState) {
        if (authState.authState == "Authenticated") {
            rideViewModel.getAllRides()
            rideViewModel.getTrips()
        } else if (authState.authState == "Unauthenticated") {
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

                    Text("Kongsi Kereta Ride Management")

                    IconButton(
                        onClick = { profileClick() },
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
                modifier = Modifier
                    .width(300.dp)
                    .height(600.dp)
            ) {
                items(rides) { ride ->
                    val driver = rideViewModel.getDriverDetails(ride)
                    Spacer(Modifier.height(20.dp))
                    ElevatedCard {
                        Spacer(Modifier.height(10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.width(300.dp)
                        ) {
                            AsyncImage(
                                model = driver.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                            Text(driver.name)
                            IconButton(
                                onClick = { /*TODO*/ }
                            ) {
                                Icon(Icons.Default.Info, contentDescription = null)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        Divider(thickness = 1.dp, color = Color.Black, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(10.dp))

                        Text("Origin: ${ride.origin}", modifier = Modifier.padding(start = 10.dp))
                        Text("Destination: ${ride.destination}", modifier = Modifier.padding(start = 10.dp))
                        Text("Date: ${ride.date}", modifier = Modifier.padding(start = 10.dp))
                        Text("Time: ${ride.time}", modifier = Modifier.padding(start = 10.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.width(300.dp)
                        ) {
                            Text("Fare: ${toRm(ride.fare)}", modifier = Modifier.padding(start = 10.dp))
                            TextButton(
                                onClick = {
                                    if (rideViewModel.checkTrips(ride, authViewModel.userIc)) {
                                        rideViewModel.removePassenger(ride, driver.ic)
                                        rideViewModel.removeTrip(
                                            RideViewModel.TripDetails(
                                                rideId = ride.rideId,
                                                riderIc = authViewModel.userIc,
                                                driverIc = driver.ic
                                            )
                                        )
                                    }
                                    else {
                                        rideViewModel.addPassenger(ride, driver.ic)
                                        rideViewModel.addTrip(
                                            RideViewModel.TripDetails(
                                                rideId = ride.rideId,
                                                riderIc = authViewModel.userIc,
                                                driverIc = driver.ic
                                            )
                                        )
                                    }
                                },
                                enabled = (ride.passengers < (driver.capacity.toInt() - 1)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (rideViewModel.checkTrips(ride, authViewModel.userIc)) {
                                        Color.Red
                                    }
                                    else {
                                        toColor("#658fc4")
                                    }
                                ),
                                modifier = Modifier.padding(end = 10.dp)
                            ) {
                                Text(
                                    text = if (rideViewModel.checkTrips(ride, authViewModel.userIc)) { "Leave " } else { "Join " }
                                        + "${ride.passengers}/${driver.capacity.toInt() - 1}",
                                    color = Color.White,
                                    modifier = Modifier
                                )
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}