package com.example.finmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finmate.navigation.Screen
import com.example.finmate.ui.component.FinanceItem
import com.example.finmate.ui.theme.*
import com.example.finmate.ui.viewmodel.AuthViewModel
import com.example.finmate.ui.viewmodel.FinanceViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceListScreen(
    navController: NavController,
    viewModel: FinanceViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
) {
    val finances by viewModel.allFinances.collectAsState()
    val initialBalance by viewModel.initialBalance.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val dailyAverage by viewModel.dailyAverage.collectAsState()
    val daysLeft by viewModel.daysLeft.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var showBalanceDialog by remember { mutableStateOf(false) }
    var tempBalance by remember { mutableStateOf(initialBalance.toString()) }
    
    val filteredFinances = finances.filter { 
        it.title.contains(searchQuery, ignoreCase = true) || 
        it.category.contains(searchQuery, ignoreCase = true) 
    }.take(5) // Show only 5 in "Beranda"

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Selamat pagi \uD83D\uDC4B", color = TextGray, fontSize = 14.sp)
                    Text(currentUser?.name ?: "User", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(PurplePrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentUser?.name?.take(2)?.uppercase() ?: "??",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Add.route) },
                containerColor = PurplePrimary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MainBalanceCard(initialBalance, currentBalance, totalExpense) {
                    tempBalance = initialBalance.toString()
                    showBalanceDialog = true
                }
            }

            item {
                QuickStatsGrid(totalExpense, dailyAverage, daysLeft, finances.size)
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Transaksi Terbaru", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    TextButton(onClick = { navController.navigate(Screen.History.route) }) {
                        Text("Lihat semua →", color = PurpleSecondary)
                    }
                }
            }

            items(filteredFinances) { finance ->
                FinanceItem(
                    finance = finance,
                    onFavoriteClick = { viewModel.updateFinance(finance.copy(isFavorite = !finance.isFavorite)) },
                    onDeleteClick = { viewModel.deleteFinance(finance) }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showBalanceDialog) {
        AlertDialog(
            onDismissRequest = { showBalanceDialog = false },
            title = { Text("Atur Saldo Awal Bulanan") },
            text = {
                OutlinedTextField(
                    value = tempBalance,
                    onValueChange = { if (it.all { char -> char.isDigit() }) tempBalance = it },
                    label = { Text("Jumlah (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.setInitialBalance(tempBalance.toIntOrNull() ?: 0)
                    showBalanceDialog = false
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showBalanceDialog = false }) { Text("Batal") }
            }
        )
    }
}

@Composable
fun MainBalanceCard(initial: Int, remaining: Int, spent: Int, onEditInitial: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onEditInitial() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(PurplePrimary, PurpleSecondary)
                    )
                )
                .padding(24.dp)
        ) {
            Column {
                val calendar = Calendar.getInstance()
                val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale("in", "ID"))?.uppercase() ?: ""
                val year = calendar.get(Calendar.YEAR)
                
                Text("$monthName $year", color = TextGray, fontSize = 12.sp)
                Text("Saldo Tersisa", color = Color.White.copy(alpha = 0.8f))
                Text(
                    text = formatRupiah(remaining),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Saldo Awal (Klik ubah)", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        Text(formatRupiah(initial), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Keluar", color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                        Text(formatRupiah(spent), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickStatsGrid(spentToday: Int, dailyAvg: Double, daysLeft: Int, totalTrans: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(modifier = Modifier.weight(1f), icon = Icons.Default.KeyboardArrowDown, label = "Pengeluaran hari ini", value = formatRupiah(spentToday))
            StatCard(modifier = Modifier.weight(1f), icon = Icons.Default.DateRange, label = "Rata-rata per hari", value = formatRupiah(dailyAvg.toInt()))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(modifier = Modifier.weight(1f), icon = Icons.Default.Build, label = "Sisa hari bulan ini", value = "$daysLeft hari")
            StatCard(modifier = Modifier.weight(1f), icon = Icons.Default.Info, label = "Transaksi bulan ini", value = "$totalTrans transaksi")
        }
    }
}

@Composable
fun StatCard(modifier: Modifier, icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, color = TextGray, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

fun formatRupiah(number: Int): String {
    val localeID = Locale("in", "ID")
    val format = NumberFormat.getCurrencyInstance(localeID)
    format.maximumFractionDigits = 0
    return format.format(number).replace("Rp", "Rp ")
}
