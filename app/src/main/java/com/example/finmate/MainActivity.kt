package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finmate.model.Finance
import com.example.finmate.model.FinanceSource
import com.example.finmate.ui.theme.FinMateTheme

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

    var listFinance by remember {
        mutableStateOf(FinanceSource.dummyFinance)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        TambahFinanceForm { financeBaru ->
            listFinance = listFinance + financeBaru
        }

        Spacer(modifier = Modifier.height(16.dp))

        listFinance.forEach { finance ->
            DetailFinanceScreen(finance)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TambahFinanceForm(onTambah: (Finance) -> Unit) {

    var nama by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }

    val initialCategories = listOf("Makanan", "Minuman", "Transport", "Internet")
    var categories by remember { mutableStateOf(initialCategories) }
    var expanded by remember { mutableStateOf(false) }
    var selectedKategori by remember { mutableStateOf(categories[0]) }
    
    var isAddingNewCategory by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

    Column {

        Text(
            text = "Tambah Transaksi",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = nama,
            onValueChange = { nama = it },
            label = { Text("Nama Transaksi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = if (isAddingNewCategory) "Tambah Kategori Baru..." else selectedKategori,
                onValueChange = {},
                readOnly = true,
                label = { Text("Pilih Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            selectedKategori = selectionOption
                            isAddingNewCategory = false
                            expanded = false
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text("+ Tambah Kategori Baru") },
                    onClick = {
                        isAddingNewCategory = true
                        expanded = false
                    }
                )
            }
        }

        if (isAddingNewCategory) {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text("Nama Kategori Baru") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

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
                val finalKategori = if (isAddingNewCategory) newCategoryName else selectedKategori
                
                if (nama.isNotEmpty() && finalKategori.isNotEmpty() && harga.isNotEmpty()) {
                    val financeBaru = Finance(
                        judul = nama,
                        kategori = finalKategori,
                        jumlah = harga.toIntOrNull() ?: 0,
                        tanggal = "Hari ini",
                        imageRes = when (finalKategori.lowercase()) {
                            "makanan" -> R.drawable.makan
                            "minuman" -> R.drawable.minuman
                            "transport" -> R.drawable.transport
                            "internet" -> R.drawable.internet
                            else -> R.drawable.makan // Default icon
                        }
                    )


                    if (isAddingNewCategory && !categories.contains(finalKategori)) {
                        categories = categories + finalKategori
                    }

                    onTambah(financeBaru)

                    nama = ""
                    harga = ""
                    newCategoryName = ""
                    isAddingNewCategory = false
                    selectedKategori = categories[0]
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Transaksi")
        }
    }
}

@Composable
fun DetailFinanceScreen(finance: Finance) {

    var isFavorite by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painterResource(id = finance.imageRes),
                contentDescription = finance.judul,
                modifier = Modifier.size(100.dp)
            )

            IconButton(
                onClick = {
                    isFavorite = !isFavorite
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite)
                        Icons.Filled.Favorite
                    else
                        Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = (if (isFavorite) Color.Red else Color.Black) as Color
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = finance.judul,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = "Kategori: ${finance.kategori}")
            Text(text = "Tanggal: ${finance.tanggal}")
            Text(text = "Harga: Rp ${finance.jumlah}")
            
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tambah Catatan")
            }
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