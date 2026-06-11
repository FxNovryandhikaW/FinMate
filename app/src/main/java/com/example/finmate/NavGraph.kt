package com.example.finmate

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.example.finmate.navigation.Screen
import com.example.finmate.ui.screen.*
import com.example.finmate.ui.theme.*
import com.example.finmate.ui.viewmodel.AuthViewModel
import com.example.finmate.ui.viewmodel.FinanceViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val financeViewModel: FinanceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    FinMateTheme {
        Scaffold(
            bottomBar = {
                if (isLoggedIn) {
                    NavigationBar(containerColor = DarkSurface) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        
                        val items = listOf(
                            Triple(Screen.Home, "Beranda", Icons.Default.Home),
                            Triple(Screen.Statistics, "Statistik", Icons.Default.Info),
                            Triple(Screen.Add, "Tambah", Icons.Default.Add),
                            Triple(Screen.History, "Riwayat", Icons.Default.List),
                            Triple(Screen.Settings, "Setelan", Icons.Default.Settings)
                        )

                        items.forEach { (screen, label, icon) ->
                            NavigationBarItem(
                                icon = { Icon(icon, contentDescription = null) },
                                label = { Text(label, fontSize = 10.sp) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                onClick = { 
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = PurplePrimary,
                                    selectedTextColor = PurplePrimary,
                                    unselectedIconColor = TextGray,
                                    unselectedTextColor = TextGray,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(navController, authViewModel)
                }
                composable(Screen.Register.route) {
                    RegisterScreen(navController, authViewModel)
                }
                composable(Screen.Home.route) {
                    FinanceListScreen(navController, financeViewModel, authViewModel)
                }
                composable(Screen.Statistics.route) {
                    StatisticsScreen(financeViewModel)
                }
                composable(Screen.Add.route) {
                    AddFinanceScreen(navController, financeViewModel)
                }
                composable(Screen.History.route) {
                    HistoryScreen(financeViewModel)
                }
                composable(Screen.Settings.route) {
                    SettingsScreen(financeViewModel, authViewModel)
                }
            }
        }
    }
}
