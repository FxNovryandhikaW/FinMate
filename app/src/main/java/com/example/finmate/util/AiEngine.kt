package com.example.finmate.util

import com.example.finmate.model.Finance
import java.text.SimpleDateFormat
import java.util.*

object AiEngine {

    /**
     * Menghitung rata-rata pengeluaran harian menggunakan Moving Average sederhana.
     * Mengambil data transaksi dan menghitung rata-rata per hari yang unik.
     */
    fun calculateDailyAverage(finances: List<Finance>): Double {
        val expenses = finances.filter { it.isExpense }
        if (expenses.isEmpty()) return 0.0

        val totalExpense = expenses.sumOf { it.amount }
        val uniqueDays = expenses.map { it.date }.distinct().size
        
        return if (uniqueDays == 0) 0.0 else totalExpense.toDouble() / uniqueDays
    }

    /**
     * Memprediksi estimasi hari sampai uang habis berdasarkan saldo saat ini.
     */
    fun predictDaysLeft(currentBalance: Int, dailyAverage: Double): Int {
        if (dailyAverage <= 0) return 0
        if (currentBalance <= 0) return 0
        return (currentBalance / dailyAverage).toInt()
    }

    /**
     * Memberikan rekomendasi penghematan berdasarkan kategori pengeluaran terbesar.
     */
    fun getSavingRecommendation(finances: List<Finance>, balance: Int): String {
        val expenses = finances.filter { it.isExpense }
        if (expenses.isEmpty()) {
            return if (balance > 0) "Saldo Anda Rp $balance. Mulai catat pengeluaran untuk melihat analisis AI!"
            else "Mulai catat transaksi untuk mendapatkan rekomendasi!"
        }

        val categoryTotals = expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }
        
        val topCategory = categoryTotals.maxByOrNull { it.value }?.key
        
        return when {
            balance <= 0 -> "Saldo Anda habis! Segera evaluasi pengeluaran $topCategory dan cari sumber pemasukan tambahan."
            balance < 500000 -> "Saldo Anda kritis (Rp $balance). Kurangi pengeluaran di kategori $topCategory dan fokus pada kebutuhan pokok."
            topCategory == "Makanan" -> "Pengeluaran makan Anda cukup tinggi. Coba kurangi makan di luar untuk menghemat lebih banyak."
            topCategory == "Belanja" -> "Kategori belanja mendominasi. Pertimbangkan untuk menunda pembelian non-primer."
            else -> "Pola pengeluaran Anda cukup stabil. Saldo Rp $balance masih dalam batas aman. Pertahankan!"
        }
    }
}
