package com.example.finmate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "finance")
data class FinanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int, // Link to UserEntity
    val title: String,
    val category: String,
    val amount: Int,
    val date: String,
    val imageUrl: String = "",
    val iconName: String = "category",
    val isFavorite: Boolean = false,
    val isExpense: Boolean = true
)
