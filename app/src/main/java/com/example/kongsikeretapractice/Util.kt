package com.example.kongsikeretapractice

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun toColor(color: String): Color {
    return Color(color.toColorInt())
}

fun toRm(amount: Double): String {
    return NumberFormat.getCurrencyInstance(Locale("en", "MY")).format(amount)
}

fun toAmount(amount: String): Double {
    var newAmount = amount.replace("RM", "")
    newAmount = newAmount.replace(",", "")
    return newAmount.toDoubleOrNull() ?: 0.0
}

fun formatDate(date: Long): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    return dateFormat.format(date)
}