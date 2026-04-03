package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.model.Finance
import com.example.finmate.model.FinanceSource
import com.example.finmate.ui.theme.FinMateTheme
import java.util.Calendar
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FinMateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DaftarFinanceScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun DaftarFinanceScreen(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
    val year = calendar.get(Calendar.YEAR)
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val remainingDaysInMonth = daysInMonth - dayOfMonth

    var listFinance by remember {
        mutableStateOf(FinanceSource.dummyFinance)
    }
    
    var saldoAwal by remember { mutableIntStateOf(0) }
    var inputSaldo by remember { mutableStateOf("") }
    
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
                        saldoAwal = inputSaldo.toIntOrNull() ?: 0
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

        TambahFinanceForm { financeBaru ->
            listFinance = listOf(financeBaru) + listFinance
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahFinanceForm(onTambah: (Finance) -> Unit) {
    var nama by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    
    val calendar = Calendar.getInstance()
    val dateString = "${calendar.get(Calendar.DAY_OF_MONTH)} ${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} ${calendar.get(Calendar.YEAR)}"

    val availableIcons = listOf(
        R.drawable.makan,
        R.drawable.minuman,
        R.drawable.transport,
        R.drawable.internet
    )
    var selectedIcon by remember { mutableIntStateOf(availableIcons[0]) }

    val categories = listOf("Makanan", "Minuman", "Transport", "Internet", "Lainnya")
    var selectedKategori by remember { mutableStateOf(categories[0]) }
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Catat Transaksi Baru", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tanggal: $dateString",
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text("Keterangan Transaksi") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Pilih Ikon Visual:", style = MaterialTheme.typography.labelLarge)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                availableIcons.forEach { iconRes ->
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .border(
                                width = if (selectedIcon == iconRes) 2.dp else 1.dp,
                                color = if (selectedIcon == iconRes) MaterialTheme.colorScheme.primary else Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedIcon = iconRes }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = selectedKategori,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Kategori") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                selectedKategori = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = harga,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        harga = it
                    }
                },
                label = { Text("Harga") },
                prefix = { Text("Rp ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nama.isNotEmpty() && harga.isNotEmpty()) {
                        val financeBaru = Finance(
                            judul = nama,
                            kategori = selectedKategori,
                            jumlah = harga.toIntOrNull() ?: 0,
                            tanggal = dateString,
                            imageRes = selectedIcon
                        )
                        onTambah(financeBaru)
                        nama = ""
                        harga = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}

@Composable
fun DetailFinanceScreen(finance: Finance) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = finance.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.size(24.dp).align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = finance.judul,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${finance.tanggal} • ${finance.kategori}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Rp ${finance.jumlah}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFinance() {
    FinMateTheme {
        DaftarFinanceScreen()
    }
}