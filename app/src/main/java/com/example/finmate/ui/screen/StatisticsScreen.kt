package com.example.finmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.ui.theme.*
import com.example.finmate.ui.viewmodel.FinanceViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(viewModel: FinanceViewModel) {
    val finances by viewModel.allFinances.collectAsState()
    val dailyAverage by viewModel.dailyAverage.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()
    val activeReminder by viewModel.reminder.collectAsState()
    
    var showReminderDialog by remember { mutableStateOf(false) }
    var reminderText by remember { mutableStateOf("") }

    LaunchedEffect(activeReminder) {
        reminderText = activeReminder
    }

    val expenseFinances = finances.filter { it.isExpense }
    val categoryTotals = expenseFinances.groupBy { it.category }
        .mapValues { entry -> entry.value.sumOf { it.amount } }
    
    val totalExpense = categoryTotals.values.sum()

    if (showReminderDialog) {
        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("Edit Reminder") },
            text = {
                TextField(
                    value = reminderText,
                    onValueChange = { reminderText = it },
                    placeholder = { Text("Contoh: Bayar tagihan...") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setReminder(reminderText)
                    showReminderDialog = false
                }) { Text("Simpan") }
            },
            dismissButton = {
                TextButton(onClick = { showReminderDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Statistik", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                AssistChip(
                    onClick = { },
                    label = { 
                        val sdf = SimpleDateFormat("MMM yyyy", Locale.getDefault())
                        Text(sdf.format(Date())) 
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = CardSurface)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                MonthlyChart(expenseFinances)
            }
            
            item {
                CategorySection(categoryTotals, totalExpense)
            }

            item {
                AiPredictionCard(dailyAverage, totalExpense, currentBalance)
            }

            item {
                ReminderCard(activeReminder) {
                    reminderText = activeReminder
                    showReminderDialog = true
                }
            }
            
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Export PDF")
                    }
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = CardSurface),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Export Excel")
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun MonthlyChart(expenses: List<com.example.finmate.model.Finance>) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agt", "Sep", "Okt", "Nov", "Des")
    
    val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val monthlyTotals = expenses.groupBy { 
        try {
            val date = sdf.parse(it.date)
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
                cal.get(Calendar.MONTH)
            } else {
                -1
            }
        } catch (e: Exception) {
            -1
        }
    }.mapValues { it.value.sumOf { f -> f.amount } }

    val maxTotal = (monthlyTotals.values.maxOrNull() ?: 1).toFloat()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("PENGELUARAN BULANAN (RP)", color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                months.indices.forEach { index ->
                    val total = monthlyTotals[index] ?: 0
                    val height = if (total > 0) (total / maxTotal).coerceAtLeast(0.05f) else 0f
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(height)
                                .background(
                                    if (height > 0) PurplePrimary else Color.Transparent,
                                    RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(months[index], fontSize = 8.sp, color = TextGray)
                    }
                }
            }
        }
    }
}

@Composable
fun CategorySection(categoryTotals: Map<String, Int>, total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("KATEGORI PENGELUARAN", color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(16.dp))
            if (categoryTotals.isEmpty()) {
                Text("Belum ada data pengeluaran", color = TextGray, fontSize = 14.sp)
            } else {
                categoryTotals.forEach { (category, amount) ->
                    val percentage = if (total > 0) (amount * 100 / total) else 0
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(8.dp).background(PurplePrimary, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(category, fontSize = 14.sp)
                        }
                        Text("$percentage%", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AiPredictionCard(dailyAvg: Double, totalExpense: Int, balance: Int) {
    val calendar = Calendar.getInstance()
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val remainingDays = (daysInMonth - dayOfMonth).coerceAtLeast(0)
    
    val predictedRemaining = dailyAvg * remainingDays
    
    val status = if (predictedRemaining > balance) "Hati-hati! " else "Bagus! "
    val recommendation = if (predictedRemaining > balance) 
        "Pengeluaran diprediksi melebihi saldo. Kurangi belanja non-pokok."
    else "Pengeluaran bulan ini diprediksi aman. Potensi sisa saldo Rp ${(balance - predictedRemaining).toInt()}."

    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, PurplePrimary.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = DarkBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = PurpleTertiary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Prediksi Keuangan AI", color = PurpleTertiary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$status$recommendation Rata-rata harian: Rp ${dailyAvg.toInt()}.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ReminderCard(text: String, onEdit: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF332211)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFFFFCC00))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Reminder Aktif", color = Color(0xFFFFCC00), fontWeight = FontWeight.Bold)
                Text(if (text.isEmpty()) "Ketuk untuk tambah reminder" else text, color = Color(0xFFFFCC00).copy(alpha = 0.8f), fontSize = 12.sp)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = TextGray, modifier = Modifier.size(20.dp))
            }
        }
    }
}
