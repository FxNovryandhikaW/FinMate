package com.example.finmate.model

import com.google.gson.annotations.SerializedName

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
