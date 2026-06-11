package com.example.finmate.navigation
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Add : Screen("add")
    object Statistics : Screen("statistics")
    object History : Screen("history")
    object Settings : Screen("settings")
}