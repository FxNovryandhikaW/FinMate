package com.example.finmate.ui.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.R
import com.example.finmate.model.Finance
import java.util.Calendar
import java.util.Locale

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
                                width = if (selectedIcon == iconRes) 3.dp else 1.dp,
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
                    label = { Text("Pilih Kategori") },
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