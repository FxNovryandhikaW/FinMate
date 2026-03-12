package com.example.finmate.model

import androidx.annotation.DrawableRes

data class Finance(
    val judul: String,
    val kategori: String,
    val jumlah: Int,
    val tanggal: String,
    @DrawableRes val imageRes: Int
)