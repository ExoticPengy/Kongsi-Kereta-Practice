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
    private val users = mutableListOf<RegisterViewModel.DriverInfo>()

    var ic by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    init {
        driversRef.get().addOnSuccessListener { documents ->
            for (document in documents) {
                users.add(document.toObject<RegisterViewModel.DriverInfo>())
            }
        }
    }

    fun getCredentials() {
        for (user in users) {
            if (user.ic == ic) {
                email = user.email
            }
        }
    }
}