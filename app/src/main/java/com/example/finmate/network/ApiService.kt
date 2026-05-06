package com.example.finmate.network

import com.example.finmate.model.Finance
import retrofit2.http.GET

interface ApiService {
    @GET("finance.json") // Sesuaikan dengan nama file di Gist Anda
    suspend fun getFinance(): List<Finance>
}
