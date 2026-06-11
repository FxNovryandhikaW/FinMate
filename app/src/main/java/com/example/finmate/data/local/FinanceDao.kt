package com.example.finmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM finance WHERE userId = :userId ORDER BY date DESC")
    fun getAllFinances(userId: Int): Flow<List<FinanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceEntity)

    @Update
    suspend fun updateFinance(finance: FinanceEntity)

    @Delete
    suspend fun deleteFinance(finance: FinanceEntity)

    @Query("SELECT * FROM finance WHERE userId = :userId AND isFavorite = 1")
    fun getFavoriteFinances(userId: Int): Flow<List<FinanceEntity>>

    @Query("SELECT SUM(amount) FROM finance WHERE userId = :userId AND isExpense = 1")
    fun getTotalExpense(userId: Int): Flow<Int?>

    @Query("SELECT SUM(amount) FROM finance WHERE userId = :userId AND isExpense = 0")
    fun getTotalIncome(userId: Int): Flow<Int?>
}
