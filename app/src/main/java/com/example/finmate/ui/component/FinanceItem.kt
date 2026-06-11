package com.example.finmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finmate.model.Finance
import com.example.finmate.ui.theme.*

@Composable
fun FinanceItem(
    finance: Finance,
    onFavoriteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val icon = getFinanceIcon(finance.category)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(DarkBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = finance.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    text = "${finance.category} • ${finance.date}",
                    fontSize = 12.sp,
                    color = TextGray
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${if (finance.isExpense) "-Rp " else "+Rp "}${finance.amount}",
                    fontWeight = FontWeight.Bold,
                    color = if (finance.isExpense) ExpenseRed else IncomeGreen
                )
                Row {
                    if (finance.isFavorite) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = GoldFavorite, modifier = Modifier.size(16.dp))
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = TextGray, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

fun getFinanceIcon(category: String): ImageVector {
    return when (category) {
        "Makanan" -> Icons.Default.Home
        "Transport" -> Icons.Default.Place
        "Belanja" -> Icons.Default.ShoppingCart
        "Kesehatan" -> Icons.Default.Add
        "Hiburan" -> Icons.Default.ThumbUp
        "Pendidikan" -> Icons.Default.Edit
        else -> Icons.Default.List
    }
}
