package com.example.finmate

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Add : Screen("add")
}