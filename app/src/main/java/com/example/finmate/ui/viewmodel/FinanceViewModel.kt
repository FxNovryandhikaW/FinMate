package com.example.finmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.finmate.data.FinanceRepository
import com.example.finmate.data.local.FinanceDatabase
import com.example.finmate.data.local.PreferenceManager
import com.example.finmate.model.Finance
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        FinanceDatabase::class.java, "finmate-db"
    ).fallbackToDestructiveMigration().build()
    
    private val repository = FinanceRepository(
        db.financeDao(),
        PreferenceManager(application)
    )

    val userId: StateFlow<Int> = repository.userId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), -1)

    val allFinances: StateFlow<List<Finance>> = userId.flatMapLatest { id ->
        if (id == -1) flowOf(emptyList()) else repository.getAllFinances(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalExpense: StateFlow<Int> = userId.flatMapLatest { id ->
        if (id == -1) flowOf(0) else repository.getTotalExpense(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalIncome: StateFlow<Int> = userId.flatMapLatest { id ->
        if (id == -1) flowOf(0) else repository.getTotalIncome(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val initialBalance: StateFlow<Int> = userId.flatMapLatest { id ->
        if (id == -1) flowOf(0) else repository.getInitialBalance(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val currentBalance: StateFlow<Int> = combine(initialBalance, totalIncome, totalExpense) { initial, income, expense ->
        initial + income - expense
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val dailyAverage: StateFlow<Double> = userId.flatMapLatest { id ->
        if (id == -1) flowOf(0.0) else repository.getDailyExpenseAverage(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val daysLeft: StateFlow<Int> = combine(currentBalance, dailyAverage) { balance, average ->
        repository.getEstimatedDaysLeft(balance, average)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val recommendation: StateFlow<String> = combine(allFinances, currentBalance) { finances, balance ->
        repository.getRecommendation(finances, balance)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Memuat rekomendasi...")

    val reminder: StateFlow<String> = userId.flatMapLatest { id ->
        if (id == -1) flowOf("") else repository.getReminder(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    fun addFinance(finance: Finance) {
        viewModelScope.launch {
            repository.insertFinance(finance)
        }
    }

    fun updateFinance(finance: Finance) {
        viewModelScope.launch {
            repository.updateFinance(finance)
        }
    }

    fun deleteFinance(finance: Finance) {
        viewModelScope.launch {
            repository.deleteFinance(finance)
        }
    }

    fun setInitialBalance(balance: Int) {
        viewModelScope.launch {
            if (userId.value != -1) {
                repository.setInitialBalance(userId.value, balance)
            }
        }
    }

    fun setReminder(text: String) {
        viewModelScope.launch {
            if (userId.value != -1) {
                repository.setReminder(userId.value, text)
            }
        }
    }
}
