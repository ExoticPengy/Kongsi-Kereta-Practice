package com.example.kongsikeretapractice

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog

@Composable
fun LoadingDialog() {
    Surface() {
        Dialog(onDismissRequest = {  }) {
            CircularProgressIndicator()
        }
    }
}