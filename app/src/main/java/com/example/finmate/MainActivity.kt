package com.example.finmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.navigation.compose.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

    val listFinance = FinanceSource.dummyFinance

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "FinMate",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        listFinance.forEach { finance ->
            DetailFinanceScreen(finance)
            Spacer(modifier = Modifier.height(24.dp))
        }

    }
}

@Composable
fun DetailFinanceScreen(finance: Finance) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(id = finance.imageRes),
            contentDescription = finance.judul,
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = finance.judul)
            Text(text = "Kategori: ${finance.kategori}")
            Text(text = "Jumlah: Rp ${finance.jumlah}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {},
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