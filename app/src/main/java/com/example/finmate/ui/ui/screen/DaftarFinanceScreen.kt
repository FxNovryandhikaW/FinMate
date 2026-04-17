package com.example.finmate.ui.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finmate.model.FinanceSource
import com.example.finmate.viewModel.FinanceViewModel
import java.util.Calendar
import java.util.Locale

@Composable
fun DaftarFinanceScreen(
    navController: NavController,
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    val calendar = Calendar.getInstance()
    val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
    val year = calendar.get(Calendar.YEAR)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val remainingDaysInMonth = daysInMonth - dayOfMonth

    var inputSaldo by remember { mutableStateOf("") }

    val listFinance = viewModel.listFinance
    val saldoAwal = viewModel.saldoAwal

    val totalPengeluaran = listFinance.sumOf { it.jumlah }
    val sisaSaldo = saldoAwal - totalPengeluaran

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Laporan Bulan $monthName $year",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Tersisa $remainingDaysInMonth hari lagi menuju akhir bulan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Atur Saldo $monthName", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = inputSaldo,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            inputSaldo = it
                        }
                    },
                    label = { Text("Masukkan Saldo (Rp)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Button(
                    onClick = {
                        val newValue = inputSaldo.toIntOrNull() ?: 0
                        viewModel.setSaldo(newValue)
                        inputSaldo = ""
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text("Simpan Saldo")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = "Sisa Saldo", fontSize = 12.sp)
                Text(
                    text = "Rp $sisaSaldo",
                    style = MaterialTheme.typography.titleLarge,
                    color = if (sisaSaldo < 0)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Terpakai", fontSize = 12.sp)
                Text(
                    text = "Rp $totalPengeluaran",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
                onClick = {
                    navController.navigate("add")
                },
        modifier = Modifier.fillMaxWidth()
        ) {
        Text("Tambah Transaksi")
    }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Riwayat Transaksi",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        listFinance.forEach { finance ->
            DetailFinanceScreen(finance)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}