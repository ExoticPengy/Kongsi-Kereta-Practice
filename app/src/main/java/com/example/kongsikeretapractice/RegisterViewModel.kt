package com.example.kongsikeretapractice

import android.content.Context
import android.net.Uri
import android.widget.Toast
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
        val driverInfo: DriverInfo = DriverInfo(),
    )

    data class DriverInfo(
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

    var selectedImageUri by mutableStateOf<Uri?>(null)
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordView by mutableStateOf(false)

    val darkColor: String = "#658fc4"

    var currentProgress by mutableFloatStateOf(0f)
    var box1 by mutableStateOf(toColor(darkColor))
    var box2 by mutableStateOf(Color.LightGray)
    var box3 by mutableStateOf(Color.LightGray)
    var step by mutableIntStateOf(1)
    var back by mutableStateOf(false)
    var buttonEnabled by mutableStateOf(true)

    fun updateUiState(DriverInfo: DriverInfo) {
        _uiState.value = RegisterUiState(
            DriverInfo
        )
    }

    fun changeProgressColor(enabled: Boolean): Color {
        return if (enabled) {
            toColor(darkColor)
        } else {
            Color.LightGray
        }
    }

    suspend fun barAnimation(start: Int, end: Int, box: Int) {
        buttonEnabled = false
        if (start < 0) {
            when (box) {
                2 -> {
                    box2 = changeProgressColor(false)
                }

                else -> {
                    box3 = changeProgressColor(false)
                }
            }
        }
        for (i in start..end) {
            currentProgress = (i.toFloat().absoluteValue / 100)
            delay(1)
        }
        if (start >= 0) {
            when (box) {
                2 -> {
                    box2 = changeProgressColor(true)
                }

                else -> {
                    box3 = changeProgressColor(true)
                }
            }
        }
        buttonEnabled = true
    }

    fun uploadProfile(context: Context) {
        val contentResolver = context.contentResolver

        // Upload image to Firebase Storage
        if (selectedImageUri != null) {
            val storageRef = Firebase.storage.reference.child("UserProfiles")
            val imageRef = storageRef.child("${_uiState.value.driverInfo.ic}.png")

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
                                _uiState.value.driverInfo.copy(imageUrl = uri.toString())
                            )
                            Toast.makeText(
                                context,
                                "Image uploaded successfully.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    context,
                    "Failed to upload image.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                context,
                "Failed to load image.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun saveDriver(driverInfo: DriverInfo) {
        db.collection("drivers")
            .add(driverInfo)
    }
}
