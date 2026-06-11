package com.example.finmate.model

import com.example.finmate.data.local.FinanceEntity

data class Finance(
    val id: Int = 0,
    val userId: Int,
    val title: String,
    val category: String,
    val amount: Int,
    val date: String,
    val imageUrl: String = "",
    val iconName: String = "category",
    val isFavorite: Boolean = false,
    val isExpense: Boolean = true
)

fun Finance.toEntity() = FinanceEntity(
    id = id,
    userId = userId,
    title = title,
    category = category,
    amount = amount,
    date = date,
    imageUrl = imageUrl,
    iconName = iconName,
    isFavorite = isFavorite,
    isExpense = isExpense
)

fun FinanceEntity.toModel() = Finance(
    id = id,
    userId = userId,
    title = title,
    category = category,
    amount = amount,
    date = date,
    imageUrl = imageUrl,
    iconName = iconName,
    isFavorite = isFavorite,
    isExpense = isExpense
)

sealed class FinanceUiState {
    object Loading : FinanceUiState()
    data class Success(val data: List<Finance>) : FinanceUiState()
    data class Error(val message: String) : FinanceUiState()
}
