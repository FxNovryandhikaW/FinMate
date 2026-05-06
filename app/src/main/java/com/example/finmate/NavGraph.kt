package com.example.finmate

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finmate.model.Finance
import com.example.finmate.navigation.Screen
import com.example.finmate.ui.ui.screen.DaftarFinanceScreen
import com.example.finmate.ui.ui.screen.TambahFinanceScreen
import com.example.finmate.viewModel.FinanceViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val viewModel: FinanceViewModel = viewModel()
    
    // State global untuk menampung data dari API (Langkah 7)
    var finances by remember { mutableStateOf<List<Finance>>(emptyList()) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                DaftarFinanceScreen(navController, viewModel) { fetchedData ->
                    finances = fetchedData
                }
            }
            composable(Screen.Add.route) {
                TambahFinanceScreen(navController, viewModel)
            }
            
            // Contoh implementasi detail jika diperlukan (Langkah 7)
            composable("detail/{judul}") { backStackEntry ->
                val judul = backStackEntry.arguments?.getString("judul")
                val finance = finances.find { it.judul == judul }
                // Tampilkan Detail Screen di sini
            }
        }
    }
}
