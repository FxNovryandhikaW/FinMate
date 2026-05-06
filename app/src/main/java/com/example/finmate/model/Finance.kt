package com.example.finmate.model

import com.google.gson.annotations.SerializedName

/**
 * Representasi data transaksi keuangan.
 */
data class Finance(
    @SerializedName("nama")
    val judul: String,
    
    @SerializedName("deskripsi")
    val kategori: String,
    
    @SerializedName("harga")
    val jumlah: Int,
    
    @SerializedName("tanggal")
    val tanggal: String = "Hari ini",

    @SerializedName(value = "image_url", alternate = ["image_name"])
    val imageUrl: String = ""
)

/**
 * State untuk merepresentasikan kondisi UI pada layar daftar transaksi.
 */
sealed class FinanceUiState {
    object Loading : FinanceUiState()
    data class Success(val data: List<Finance>) : FinanceUiState()
    data class Error(val message: String) : FinanceUiState()
}
