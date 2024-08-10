package com.example.kongsikeretapractice

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.absoluteValue

class RegisterViewModel: ViewModel() {
    data class RegisterUiState(
        val userInfo: UserInfo = UserInfo(),
        val vehicleInfo: VehicleInfo = VehicleInfo()
    )

    data class UserInfo(
        var uid: String = "",
        var name: String = "",
        var ic: String = "",
        var gender: String = "",
        var phone: String = "",
        var email: String = "",
        var address: String = "",
        var imageUrl: String = "",
    )

    data class VehicleInfo(
        var model: String = "",
        var capacity: String = "",
        var special: String = ""
    )

    data class DriverInfo(
        var uid: String = "",
        var name: String = "",
        var ic: String = "",
        var gender: String = "",
        var phone: String = "",
        var email: String = "",
        var address: String = "",
        var imageUrl: String = "",
        var model: String = "",
        var capacity: String = "",
        var special: String = ""
    )

    private val _uiState = MutableStateFlow(RegisterUiState())

    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    val db = Firebase.firestore

    var registerAsDriver by mutableStateOf(true)

    var selectedImageUri by mutableStateOf<Uri?>(null)
    var password by mutableStateOf("")

    val darkColor: String = "#658fc4"

    var box1 by mutableStateOf(toColor(darkColor))
    var box2 by mutableStateOf(Color.LightGray)
    var box3 by mutableStateOf(Color.LightGray)
    var step by mutableIntStateOf(1)
    var buttonEnabled by mutableStateOf(true)
    var back by mutableStateOf(true)

    fun updateUiState(userInfo: UserInfo, vehicleInfo: VehicleInfo = VehicleInfo()) {
        _uiState.value = RegisterUiState(
            userInfo,
            vehicleInfo
        )
    }

    fun changeProgressColor(enabled: Boolean): Color {
        return if (enabled) {
            toColor(darkColor)
        } else {
            Color.LightGray
        }
    }

    fun changeStep(newStep: Int) {
        back = step >= newStep

        step = newStep

        buttonEnabled = false

        when (step) {
            1 -> {
                box2 = changeProgressColor(false)
                box3 = changeProgressColor(false)
            }
            2 -> {
                box2 = changeProgressColor(true)
                box3 = changeProgressColor(false)
            }
            3 -> {
                box2 = changeProgressColor(true)
                box3 = changeProgressColor(true)
            }
        }

        buttonEnabled = true
    }

    fun uploadProfile(context: Context) {
        val contentResolver = context.contentResolver

        // Upload image to Firebase Storage
        if (selectedImageUri != null) {
            val storageRef = Firebase.storage.reference.child("UserProfiles")
            val imageRef = storageRef.child("${_uiState.value.userInfo.ic}.png")

            // Read image data from Uri
            val inputStream =
                contentResolver.openInputStream(selectedImageUri!!)
            val imageBytes = inputStream?.readBytes()

            // Upload image bytes
            if (imageBytes != null) {
                imageRef.putBytes(imageBytes)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                            updateUiState(
                                _uiState.value.userInfo.copy(imageUrl = uri.toString()),
                                _uiState.value.vehicleInfo
                            )
                        }
                    }
            }
        } else {
            Toast.makeText(
                context,
                "Failed to load image.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun saveDriver() {
        val userInfo = _uiState.value.userInfo
        val vehicleInfo = _uiState.value.vehicleInfo

        val driverInfo = DriverInfo(
            uid = userInfo.uid,
            name = userInfo.name,
            ic = userInfo.ic,
            gender = userInfo.gender,
            phone = userInfo.phone,
            email = userInfo.email,
            address = userInfo.address,
            imageUrl = userInfo.imageUrl,
            model = vehicleInfo.model,
            capacity = vehicleInfo.capacity,
            special = vehicleInfo.special
        )

        db.collection("drivers")
            .add(driverInfo)
    }

    fun saveRider() {
        val userInfo = _uiState.value.userInfo

        db.collection("riders")
            .add(userInfo)
    }

    //Validation
    var emailHasErrors by mutableStateOf(false)

    fun checkEmail() {
        emailHasErrors = if (uiState.value.userInfo.email.isNotEmpty()) {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.value.userInfo.email).matches()
        }
        else false
    }

}
