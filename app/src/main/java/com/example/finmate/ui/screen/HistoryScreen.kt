package com.example.finmate.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.ui.component.FinanceItem
import com.example.finmate.ui.theme.*
import com.example.finmate.ui.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: FinanceViewModel) {
    val finances by viewModel.allFinances.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredFinances = finances.filter { 
        it.title.contains(searchQuery, ignoreCase = true) || 
        it.category.contains(searchQuery, ignoreCase = true) 
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Riwayat Transaksi", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Cari nama atau kategori...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = CardSurface,
                        focusedContainerColor = CardSurface,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    shape = MaterialTheme.shapes.medium
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatBox(modifier = Modifier.weight(1f), label = "TRANSAKSI", value = finances.size.toString(), valueColor = PurpleTertiary)
                    StatBox(modifier = Modifier.weight(1f), label = "TOTAL KELUAR", value = "Rp ${totalExpense/1000}rb", valueColor = ExpenseRed)
                    StatBox(modifier = Modifier.weight(1f), label = "SISA SALDO", value = "Rp ${currentBalance/1000}rb", valueColor = IncomeGreen)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            val grouped = filteredFinances.groupBy { it.date }
            grouped.forEach { (date, items) ->
                item {
                    Text(text = date.uppercase(), fontSize = 12.sp, color = TextGray, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(items) { finance ->
                    FinanceItem(
                        finance = finance,
                        onFavoriteClick = { viewModel.updateFinance(finance.copy(isFavorite = !finance.isFavorite)) },
                        onDeleteClick = { viewModel.deleteFinance(finance) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun StatBox(modifier: Modifier, label: String, value: String, valueColor: Color) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = TextGray)
            Text(value, fontWeight = FontWeight.Bold, color = valueColor, fontSize = 14.sp)
        }
    }
}
