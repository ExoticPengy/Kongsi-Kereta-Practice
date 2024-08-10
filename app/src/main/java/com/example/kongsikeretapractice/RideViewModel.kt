package com.example.kongsikeretapractice

import android.nfc.Tag
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RideViewModel: ViewModel() {
    data class RideUiState(
        val rides: List<RideDetails> = listOf(),
        val trips: List<TripDetails> = listOf()
    )

    data class RideDetails(
        var rideId: Int =  0,
        var date: String = "",
        var time: String = "",
        var origin: String = "",
        var destination: String = "",
        var fare: Double = 0.0,
        var driverIc: String = "",
        var passengers: Int = 0
    )

    data class TripDetails(
        var rideId: Int = 0,
        var riderIc: String = "",
        var driverIc: String = ""
    )

    private val _uiState = MutableStateFlow(RideUiState())
    val uiState: StateFlow<RideUiState> = _uiState.asStateFlow()

    private val db = Firebase.firestore
    private val ridesRef = db.collection("rides")
    private val driversRef = db.collection("drivers")
    private val tripsRef = db.collection("trips")

    var driverList = mutableListOf<RegisterViewModel.DriverInfo>()

    var userIc by mutableStateOf("")
    var newRideId by mutableIntStateOf(0)
    var currentEdit by mutableIntStateOf(-1)
    var newRideDetails by mutableStateOf(RideDetails())
    var datePicker by mutableStateOf(false)
    var timePicker by mutableStateOf(false)

    init {
        driversRef.get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    driverList.add(doc.toObject())
                }
            }
    }

    fun updateUiState(rides: List<RideDetails> = listOf(), trips: List<TripDetails> = listOf()) {
        _uiState.value = RideUiState(
            rides, trips
        )
    }

    fun getDriverDetails(ride: RideDetails):RegisterViewModel.DriverInfo {
        for (driver in driverList) {
            if (driver.ic == ride.driverIc) {
                return driver
            }
        }
        return RegisterViewModel.DriverInfo()
    }

    fun getRides() {
        ridesRef
            .orderBy("rideId", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val rideList = mutableListOf<RideDetails>()
                for (document in result) {
                    if (document.toObject<RideDetails>().driverIc == userIc) {
                        rideList.add(document.toObject())
                        if (document.toObject<RideDetails>().rideId >= newRideId) {
                            newRideId = document.toObject<RideDetails>().rideId + 1
                        }
                    }
                }
                updateUiState(rides = rideList)
            }
    }

    fun deleteRide() {
        ridesRef.whereEqualTo("driverIc", userIc).whereEqualTo("rideId", currentEdit).get()
            .addOnSuccessListener {
                for(doc in it) {
                    ridesRef.document(doc.id).delete()
                    getRides()
                }
            }
    }

    fun getAllRides() {
        ridesRef
            .get()
            .addOnSuccessListener {  docs ->
                val rideList = mutableListOf<RideDetails>()
                for (doc in docs) {
                    rideList.add(doc.toObject())
                }
                updateUiState(rides = rideList)
            }
    }

    fun newRide() {
        ridesRef
            .add(RideDetails(driverIc = userIc, rideId = newRideId))
            .addOnSuccessListener {
                newRideId++
                getRides()
            }
    }

    fun updateRide() {
        ridesRef
            .whereEqualTo("driverIc", userIc)
            .whereEqualTo("rideId", newRideDetails.rideId)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    ridesRef.document(doc.id)
                        .set(newRideDetails)
                }
                getRides()
            }
    }

    fun addPassenger(ride: RideDetails, driverIc: String) {
        ridesRef
            .whereEqualTo("driverIc", driverIc)
            .whereEqualTo("rideId", ride.rideId)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    ridesRef.document(doc.id)
                        .set(ride.copy(passengers = ride.passengers + 1))
                }
                getAllRides()
            }
    }
    fun removePassenger(ride: RideDetails, driverIc: String) {
        ridesRef
            .whereEqualTo("driverIc", driverIc)
            .whereEqualTo("rideId", ride.rideId)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    ridesRef.document(doc.id)
                        .set(ride.copy(passengers = ride.passengers - 1))
                }
                getAllRides()
            }
    }

    fun getTrips() {
        tripsRef.get()
            .addOnSuccessListener { result ->
                val tripList = mutableListOf<TripDetails>()
                for (document in result) {
                    tripList.add(document.toObject())
                }
                updateUiState(rides = _uiState.value.rides, trips = tripList)
            }
    }

    fun addTrip(trip: TripDetails) {
        tripsRef.add(trip)
            .addOnSuccessListener {
                getTrips()
            }
    }

    fun removeTrip(trip: TripDetails) {
        tripsRef.whereEqualTo("rideId", trip.rideId).whereEqualTo("driverIc", trip.driverIc).whereEqualTo("riderIc", trip.riderIc)
            .get()
            .addOnSuccessListener {
                for (doc in it) {
                    tripsRef.document(doc.id).delete()
                        .addOnSuccessListener {
                            getTrips()
                        }
                }
            }
    }

    fun checkTrips(ride: RideDetails, riderIc: String): Boolean {
        val trips = _uiState.value.trips
        for (trip in trips) {
            if (trip.rideId == ride.rideId && trip.driverIc == ride.driverIc && trip.riderIc == riderIc) {
                return true
            }
        }
        return false
    }
}