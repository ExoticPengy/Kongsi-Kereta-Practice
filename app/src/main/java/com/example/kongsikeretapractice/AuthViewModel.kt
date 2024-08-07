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

class AuthViewModel: ViewModel() {
    data class AuthState(
        val authState: String = "",
        val error: String = ""
    )

    private val _authState = MutableStateFlow(AuthState())

    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val auth = Firebase.auth

    var userIc by mutableStateOf("")

    init {
        checkAuth()
    }

    private fun updateAuthState(authState: String, error: String = "") {
        _authState.value = AuthState(authState, error)
    }

    private fun checkAuth() {
       if (auth.currentUser != null) {
           getUserIc(auth.currentUser!!.email ?: "")
           updateAuthState(authState = "Authenticated")
       }
       else {
           updateAuthState(authState = "Unauthenticated")
       }
    }

    fun logIn(ic: String, email: String, password: String) {
        updateAuthState("Loading")

        if (ic.isEmpty() || email.isEmpty() || password.isEmpty()) {
            updateAuthState(
                authState = "Empty",
                error = "IC and password can't be empty!"
            )
            return
        }

        updateAuthState(authState = "Loading")

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userIc = ic
                updateAuthState(authState = "Authenticated")
            }
            else {
                updateAuthState(
                    authState = "Unsuccessful",
                    error = task.exception?.message?:"Your IC or Password was incorrect! Please try again."
                )
            }
        }
    }

    fun signUp(email: String, password: String) {
        updateAuthState("Loading")

        if (email.isEmpty() || password.isEmpty()) {
            updateAuthState(
                authState = "Empty",
                error = "Some of your details might be empty!"
            )
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    updateAuthState("Success")
                    auth.signOut()
                } else {
                    updateAuthState(
                        "Failed",
                        task.exception?.message?:"Something went wrong with sign up."
                    )
                }
            }
    }

    fun logOut() {
        auth.signOut()
        userIc = ""
        updateAuthState("Unauthenticated")
    }

    private fun getUserIc(email: String = "") {
        val db = Firebase.firestore
        val driverRef = db.collection("drivers")

        driverRef.whereEqualTo("email", email).get().addOnSuccessListener {  docs ->
            for (doc in docs) {
                userIc = doc.toObject<RegisterViewModel.DriverInfo>().ic
            }
        }
    }
}