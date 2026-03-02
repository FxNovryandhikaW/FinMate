package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.finmate.ui.theme.FinMateTheme
import model.TransactionSource

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinMateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TransactionList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TransactionList(modifier: Modifier = Modifier) {
    val list = TransactionSource.dummyTransaction

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        list.forEach { transaksi ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = transaksi.imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp).padding(end = 16.dp)
                )
                Column {
                    Text(text = "Nama: ${transaksi.nama}")
                    Text(text = "Kategori: ${transaksi.kategori}")
                    Text(text = "Jumlah: Rp ${transaksi.jumlah}")
                }
            }
            Text(text = "------------------------------------------")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionListPreview() {
    FinMateTheme {
        TransactionList()
    }
}
