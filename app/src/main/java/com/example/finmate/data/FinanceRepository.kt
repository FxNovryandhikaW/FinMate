package com.example.finmate.data

import com.example.finmate.data.local.FinanceDao
import com.example.finmate.data.local.PreferenceManager
import com.example.finmate.model.Finance
import com.example.finmate.model.toEntity
import com.example.finmate.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FinanceRepository(
    private val financeDao: FinanceDao,
    private val preferenceManager: PreferenceManager
) {
    fun getAllFinances(userId: Int): Flow<List<Finance>> = 
        financeDao.getAllFinances(userId).map { entities -> entities.map { it.toModel() } }

    suspend fun insertFinance(finance: Finance) = financeDao.insertFinance(finance.toEntity())

    suspend fun updateFinance(finance: Finance) = financeDao.updateFinance(finance.toEntity())

    suspend fun deleteFinance(finance: Finance) = financeDao.deleteFinance(finance.toEntity())

    fun getFavoriteFinances(userId: Int): Flow<List<Finance>> = 
        financeDao.getFavoriteFinances(userId).map { entities -> entities.map { it.toModel() } }

    fun getTotalExpense(userId: Int): Flow<Int> = financeDao.getTotalExpense(userId).map { it ?: 0 }
    
    fun getTotalIncome(userId: Int): Flow<Int> = financeDao.getTotalIncome(userId).map { it ?: 0 }

    fun getInitialBalance(userId: Int): Flow<Int> = preferenceManager.getInitialBalance(userId)
    
    fun getReminder(userId: Int): Flow<String> = preferenceManager.getReminder(userId)
    
    val userId: Flow<Int> = preferenceManager.userId

    suspend fun setInitialBalance(userId: Int, balance: Int) = preferenceManager.setInitialBalance(userId, balance)

    suspend fun setReminder(userId: Int, reminder: String) = preferenceManager.setReminder(userId, reminder)

    // AI Logic delegated to AiEngine
    fun getDailyExpenseAverage(userId: Int): Flow<Double> = financeDao.getAllFinances(userId).map { list ->
        com.example.finmate.util.AiEngine.calculateDailyAverage(list.map { it.toModel() })
    }

    fun getEstimatedDaysLeft(currentBalance: Int, dailyAverage: Double): Int {
        return com.example.finmate.util.AiEngine.predictDaysLeft(currentBalance, dailyAverage)
    }

    fun getRecommendation(finances: List<Finance>, balance: Int): String {
        return com.example.finmate.util.AiEngine.getSavingRecommendation(finances, balance)
    }
}
