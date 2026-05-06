package com.example.finmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finmate.model.Finance
import com.example.finmate.model.FinanceUiState
import com.example.finmate.network.FinanceRepository
import com.example.finmate.network.RetrofitClient
import kotlinx.coroutines.launch

class FinanceViewModel : ViewModel() {

    // Dependency Injection sederhana
    private val repository = FinanceRepository(RetrofitClient.instance)

    // UI State menggunakan pola Sealed Class (Clean Architecture)
    var uiState: FinanceUiState by mutableStateOf(FinanceUiState.Loading)
        private set

    var saldoAwal by mutableIntStateOf(0)
        private set

    init {
        loadFinances()
    }

    /**
     * Mengambil data dari repository dan memperbarui UI State.
     */
    fun loadFinances() {
        viewModelScope.launch {
            uiState = FinanceUiState.Loading
            try {
                val data = repository.getFinances()
                uiState = FinanceUiState.Success(data)
            } catch (e: Exception) {
                uiState = FinanceUiState.Error(e.message ?: "Terjadi kesalahan koneksi")
            }
        }
    }

    fun tambahFinance(finance: Finance) {
        val currentState = uiState
        if (currentState is FinanceUiState.Success) {
            uiState = FinanceUiState.Success(listOf(finance) + currentState.data)
        }
    }

    fun setSaldo(saldo: Int) {
        saldoAwal = saldo
    }
}
