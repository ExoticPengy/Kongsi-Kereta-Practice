package com.example.kongsikeretapractice

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
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RideViewModel: ViewModel() {
    data class RideUiState(
        val rides: List<RideDetails> = listOf()
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

    private val _uiState = MutableStateFlow(RideUiState())
    val uiState: StateFlow<RideUiState> = _uiState.asStateFlow()

    private val db = Firebase.firestore
    private val ridesRef = db.collection("rides")

    var userIc by mutableStateOf("")
    var newRideId by mutableIntStateOf(0)
    var currentEdit by mutableIntStateOf(-1)
    var newRideDetails by mutableStateOf(RideDetails())
    var datePicker by mutableStateOf(false)
    var timePicker by mutableStateOf(false)

    fun updateUiState(rides: List<RideDetails> = listOf()) {
        _uiState.value = RideUiState(
            rides
        )
    }

    fun getRides() {
        ridesRef
            .whereEqualTo("driverIc", userIc)
            .get()
            .addOnSuccessListener { result ->
                val rideList = mutableListOf<RideDetails>()
                for (document in result) {
                    rideList.add(document.toObject())
                    if (document.toObject<RideDetails>().rideId >= newRideId) {
                        newRideId = document.toObject<RideDetails>().rideId + 1
                    }
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
}