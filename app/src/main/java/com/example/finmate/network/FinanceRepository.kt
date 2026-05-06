package com.example.finmate.network

import com.example.finmate.model.Finance

/**
 * Repository untuk memisahkan logika pengambilan data (Data Layer) dari ViewModel.
 */
class FinanceRepository(private val apiService: ApiService) {
    
    suspend fun getFinances(): List<Finance> {
        return apiService.getFinance()
    }
    
    // Anda bisa menambahkan logika simpan data ke database lokal (Room) di sini nantinya.
}
