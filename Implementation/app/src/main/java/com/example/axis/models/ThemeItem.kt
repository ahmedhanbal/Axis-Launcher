package com.example.axis.models

data class ThemeItem(
    val name: String,
    val description: String,
    val colors: List<String>,
    val isActive: Boolean = false,
    val isPremium: Boolean = false
)
