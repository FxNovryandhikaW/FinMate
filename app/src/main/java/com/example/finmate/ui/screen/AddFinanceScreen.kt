package com.example.finmate.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finmate.ui.component.AddFinanceForm
import com.example.finmate.ui.viewmodel.FinanceViewModel

@Composable
fun AddFinanceScreen(
    navController: NavController,
    viewModel: FinanceViewModel,
) {
    val userId by viewModel.userId.collectAsState()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AddFinanceForm(userId = userId) { newFinance ->
            viewModel.addFinance(newFinance)
            navController.popBackStack()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
        }
    }
}
