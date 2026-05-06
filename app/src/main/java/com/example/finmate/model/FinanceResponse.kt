package com.example.finmate.model

import com.google.gson.annotations.SerializedName

data class FinanceResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<Finance>
)
