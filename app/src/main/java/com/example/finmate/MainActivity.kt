package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import com.example.finmate.ui.ui.screen.DaftarFinanceScreen
import com.example.finmate.ui.ui.theme.FinMateTheme
import androidx.navigation.compose.*
import com.example.finmate.navigation.Screen
import com.example.finmate.ui.ui.screen.TambahFinanceScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finmate.viewModel.FinanceViewModel
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FinMateTheme {

                val navController = rememberNavController()
                val viewModel: FinanceViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        composable(Screen.Home.route) {
                            DaftarFinanceScreen(navController, viewModel)
                        }

                        composable(Screen.Add.route) {
                            TambahFinanceScreen(navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}