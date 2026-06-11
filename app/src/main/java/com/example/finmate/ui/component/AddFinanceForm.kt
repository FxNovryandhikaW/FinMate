package com.example.finmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.finmate.model.Finance
import com.example.finmate.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddFinanceForm(userId: Int, onAdd: (Finance) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Makanan") }
    var selectedIcon by remember { mutableStateOf("restaurant") }
    var note by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var isExpense by remember { mutableStateOf(true) }

    val dateString = remember { 
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
    }
    val dayName = remember { 
        SimpleDateFormat("EEEE", Locale("in", "ID")).format(Date())
    }

    val categories = if (isExpense) {
        listOf("Makanan", "Transport", "Belanja", "Kesehatan", "Hiburan", "Pendidikan")
    } else {
        listOf("Gaji", "Bonus", "Investasi", "Hadiah", "Penjualan", "Lainnya")
    }
    
    val icons = listOf("restaurant", "directions_bus", "shopping_cart", "medical_services", "sports_esports", "menu_book")

    Column(modifier = Modifier.fillMaxWidth()) {
        // Toggle Pemasukan/Pengeluaran
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(CardSurface, RoundedCornerShape(12.dp))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (isExpense) ExpenseRed else Color.Transparent)
                    .clickable { isExpense = true }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Pengeluaran", color = if (isExpense) Color.White else TextGray, fontWeight = FontWeight.Bold)
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (!isExpense) IncomeGreen else Color.Transparent)
                    .clickable { isExpense = false }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Pemasukan", color = if (!isExpense) Color.White else TextGray, fontWeight = FontWeight.Bold)
            }
        }

        // Amount Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(
                modifier = Modifier
                    .background(Brush.verticalGradient(
                        if (isExpense) listOf(ExpenseRed, ExpenseRed.copy(alpha = 0.7f))
                        else listOf(IncomeGreen, IncomeGreen.copy(alpha = 0.7f))
                    ))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("IDR — RUPIAH", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = amount,
                        onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            if (amount.isEmpty()) {
                                Text(
                                    "Rp 0",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                                    Text("Rp ", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                                    innerTextField()
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$dayName, $dateString", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("PILIH IKON", color = TextGray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(6),
            modifier = Modifier.height(50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(icons) { icon ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (selectedIcon == icon) PurplePrimary else CardSurface)
                        .clickable { selectedIcon = icon },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIcon(icon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("PILIH KATEGORI", color = TextGray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.height(120.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedCategory = category },
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedCategory == category) PurplePrimary else CardSurface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(getIconForCategory(category), contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("CATATAN", color = TextGray, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = note,
            onValueChange = { note = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Tulis catatan...") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = CardSurface,
                focusedContainerColor = CardSurface,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if (isFavorite) Color.Yellow else TextGray,
                modifier = Modifier.clickable { isFavorite = !isFavorite }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Tandai sebagai transaksi favorit", color = TextGray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                if (amount.isNotEmpty()) {
                    onAdd(Finance(
                        userId = userId,
                        title = note.ifEmpty { selectedCategory },
                        category = selectedCategory,
                        amount = amount.toIntOrNull() ?: 0,
                        date = dateString,
                        isExpense = isExpense,
                        isFavorite = isFavorite,
                        iconName = selectedIcon
                    ))
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isExpense) ExpenseRed else IncomeGreen)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Done, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan ${if (isExpense) "Pengeluaran" else "Pemasukan"}", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun getIcon(name: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when(name) {
        "restaurant" -> Icons.Default.Home
        "directions_bus" -> Icons.Default.Place
        "shopping_cart" -> Icons.Default.ShoppingCart
        "medical_services" -> Icons.Default.Add
        "sports_esports" -> Icons.Default.ThumbUp
        "menu_book" -> Icons.Default.Edit
        else -> Icons.Default.Menu
    }
}

@Composable
fun getIconForCategory(cat: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when(cat) {
        "Makanan" -> Icons.Default.Home
        "Transport" -> Icons.Default.Place
        "Belanja" -> Icons.Default.ShoppingCart
        "Kesehatan" -> Icons.Default.Add
        "Hiburan" -> Icons.Default.ThumbUp
        "Pendidikan" -> Icons.Default.Edit
        else -> Icons.Default.Menu
    }
}
