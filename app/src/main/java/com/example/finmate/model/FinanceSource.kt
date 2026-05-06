package com.example.finmate.model

import android.content.Context

object FinanceSource {
    // Fungsi untuk mencari ID drawable berdasarkan nama file (String) dari API
    // Dibuat nullable agar tidak force close jika data dari API kosong
    fun getResourceId(context: Context, imageName: String?): Int {
        if (imageName.isNullOrEmpty()) return 0
        return context.resources.getIdentifier(imageName, "drawable", context.packageName)
    }
}
