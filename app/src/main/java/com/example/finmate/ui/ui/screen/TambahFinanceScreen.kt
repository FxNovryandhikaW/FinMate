package com.example.finmate.ui.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finmate.viewModel.FinanceViewModel

@Composable
fun TambahFinanceScreen(
    navController: NavController,
    viewModel: FinanceViewModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TambahFinanceForm { financeBaru ->
            viewModel.tambahFinance(financeBaru)
            navController.popBackStack()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Kembali")
        }
    }
}