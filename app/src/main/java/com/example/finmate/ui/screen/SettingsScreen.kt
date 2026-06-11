package com.example.finmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.ui.theme.*
import com.example.finmate.ui.viewmodel.AuthViewModel
import com.example.finmate.ui.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: FinanceViewModel, authViewModel: AuthViewModel) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val initialBalance by viewModel.initialBalance.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var showBalanceDialog by remember { mutableStateOf(false) }
    var tempBalance by remember { mutableStateOf(initialBalance.toString()) }

    Scaffold(
        topBar = {
            Text(
                "Pengaturan",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(PurplePrimary), contentAlignment = Alignment.Center) {
                        Text(
                            text = currentUser?.name?.take(2)?.uppercase() ?: "??",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(currentUser?.name ?: "User", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(currentUser?.email ?: "email@example.com", color = TextGray, fontSize = 12.sp)
                        Text("✨ Premium Member", color = Color(0xFFFFCC00), fontSize = 10.sp)
                    }
                }
            }

            Text("KEUANGAN", color = TextGray, fontSize = 12.sp)
            SettingsItem(
                icon = Icons.Default.ThumbUp,
                label = "Saldo Awal Bulanan", 
                value = "Rp ${initialBalance}",
                onClick = { 
                    tempBalance = initialBalance.toString()
                    showBalanceDialog = true 
                }
            )
            SettingsItem(icon = Icons.Default.Notifications, label = "Reminder Pengeluaran", hasSwitch = true, switchState = true)
            
            Text("TAMPILAN", color = TextGray, fontSize = 12.sp)
            SettingsItem(icon = Icons.Default.Build, label = "Dark Mode", hasSwitch = true, switchState = isDarkMode, onSwitch = { viewModel.toggleDarkMode() })
            SettingsItem(icon = Icons.Default.Info, label = "Bahasa", value = "Bahasa Indonesia")

            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = { authViewModel.logout() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF331111)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = ExpenseRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Keluar Akun", color = ExpenseRed)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showBalanceDialog) {
        AlertDialog(
            onDismissRequest = { showBalanceDialog = false },
            title = { Text("Ubah Saldo Awal") },
            text = {
                OutlinedTextField(
                    value = tempBalance,
                    onValueChange = { if (it.all { char -> char.isDigit() }) tempBalance = it },
                    label = { Text("Jumlah (Rp)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            },
            confirmButton = {
                TextButton(onClick = {
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
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String? = null,
    hasSwitch: Boolean = false,
    switchState: Boolean = false,
    onSwitch: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !hasSwitch, onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = PurplePrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(label, fontSize = 14.sp)
            }
            if (hasSwitch) {
                Switch(checked = switchState, onCheckedChange = onSwitch)
            } else if (value != null) {
                Text(value, fontSize = 12.sp, color = TextGray)
            }
        }
    }
}
