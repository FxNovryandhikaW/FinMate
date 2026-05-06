package com.example.finmate.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/FxNovryandhikaW/6e0e1996629c84e2b5c8ad97e08e38db/raw/7f2df17c999183b10e4ee92e15c8dea2166e7c5a/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
