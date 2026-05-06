package com.example.finmate.ui.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finmate.model.Finance
import com.example.finmate.model.FinanceUiState
import com.example.finmate.viewModel.FinanceViewModel
import java.util.*

@Composable
fun DaftarFinanceScreen(
    navController: NavController,
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier,
) {
    val uiState = viewModel.uiState
    val saldoAwal = viewModel.saldoAwal

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        HeaderLaporan()
        
        Spacer(modifier = Modifier.height(20.dp))
        
        AturSaldoSection(
            onSaveSaldo = { viewModel.setSaldo(it) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Menangani berbagai state UI (Clean Code Pattern)
        when (uiState) {
            is FinanceUiState.Loading -> LoadingIndicator()
            is FinanceUiState.Error -> ErrorMessage(uiState.message) { viewModel.loadFinances() }
            is FinanceUiState.Success -> {
                RingkasanSaldo(saldoAwal, uiState.data)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { navController.navigate("add") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tambah Transaksi")
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                RiwayatTransaksiSection(uiState.data)
            }
        }
    }
}

@Composable
fun HeaderLaporan() {
    val calendar = Calendar.getInstance()
    val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
    val year = calendar[Calendar.YEAR]
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val remainingDays = daysInMonth - calendar[Calendar.DAY_OF_MONTH]

    Column {
        Text(text = "Laporan Bulan $monthName $year", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "Tersisa $remainingDays hari lagi menuju akhir bulan", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun AturSaldoSection(onSaveSaldo: (Int) -> Unit) {
    var inputSaldo by remember { mutableStateOf("") }
    
    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Atur Saldo", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = inputSaldo,
                onValueChange = { if (it.all { char -> char.isDigit() }) inputSaldo = it },
                label = { Text("Masukkan Saldo (Rp)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = { 
                    onSaveSaldo(inputSaldo.toIntOrNull() ?: 0)
                    inputSaldo = ""
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Text("Simpan Saldo")
            }
        }
    }
}

@Composable
fun RingkasanSaldo(saldoAwal: Int, listFinance: List<Finance>) {
    val totalPengeluaran = listFinance.sumOf { it.jumlah }
    val sisaSaldo = saldoAwal - totalPengeluaran

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text(text = "Sisa Saldo", fontSize = 12.sp)
            Text(
                text = "Rp $sisaSaldo",
                style = MaterialTheme.typography.titleLarge,
                color = if (sisaSaldo < 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "Terpakai", fontSize = 12.sp)
            Text(text = "Rp $totalPengeluaran", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RiwayatTransaksiSection(listFinance: List<Finance>) {
    Text(text = "Riwayat Transaksi", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    listFinance.forEach { finance ->
        FinanceItem(finance)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(message: String, onRetry: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) { Text("Coba Lagi") }
    }
}
