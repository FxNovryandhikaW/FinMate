package model
import androidx.annotation.DrawableRes

data class Transaction(
    val nama: String,
    val kategori: String,
    val jumlah: Int,
    @DrawableRes val imageRes: Int
)