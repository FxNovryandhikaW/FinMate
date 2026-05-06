package com.example.finmate

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finmate.model.FinanceUiState
import com.example.finmate.navigation.Screen
import com.example.finmate.ui.ui.screen.DaftarFinanceScreen
import com.example.finmate.ui.ui.screen.TambahFinanceScreen
import com.example.finmate.viewModel.FinanceViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: FinanceViewModel = viewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                DaftarFinanceScreen(navController, viewModel)
            }
            
            composable(Screen.Add.route) {
                TambahFinanceScreen(navController, viewModel)
            }
            
            // Route Detail (Opsional)
            composable("detail/{judul}") { backStackEntry ->
                val judul = backStackEntry.arguments?.getString("judul")
                val uiState = viewModel.uiState
                
                if (uiState is FinanceUiState.Success) {
                    val finance = uiState.data.find { it.judul == judul }
                    finance?.let {
                        // Tampilkan DetailFinanceScreen(it) di sini jika sudah ada
                    }
                }
            }
        }
    }
}
