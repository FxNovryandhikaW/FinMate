package com.example.finmate.data

import com.example.finmate.model.Finance
import retrofit2.http.GET

interface ApiService {
    @GET("finance.json")
    suspend fun getFinance(): List<Finance>
}
