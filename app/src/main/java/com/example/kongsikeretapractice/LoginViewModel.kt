package com.example.kongsikeretapractice

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {

    private val db = Firebase.firestore
    private val driversRef = db.collection("drivers")
    private val drivers = mutableListOf<RegisterViewModel.DriverInfo>()
    private val ridersRef = db.collection("riders")
    private val riders = mutableListOf<RegisterViewModel.UserInfo>()

    var ic by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isDriver by mutableStateOf(false)
    var showPassword by mutableStateOf(false)

    init {
        driversRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                drivers.add(document.toObject())
            }
        }
        ridersRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                riders.add(document.toObject())
            }
        }
    }

    fun getCredentials() {
        for (user in drivers) {
            if (user.ic == ic) {
                email = user.email
                isDriver = true
            }
        }
        for (user in riders) {
            if (user.ic == ic) {
                email = user.email
                isDriver = false
            }
        }
    }

    fun reset() {
        ic = ""
        email = ""
        password = ""
    }
}