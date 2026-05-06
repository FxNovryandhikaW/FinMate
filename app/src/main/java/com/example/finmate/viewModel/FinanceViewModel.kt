package com.example.finmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finmate.model.Finance
import com.example.finmate.network.RetrofitClient
import kotlinx.coroutines.launch

class FinanceViewModel : ViewModel() {

    var listFinance by mutableStateOf<List<Finance>>(emptyList())
        private set

    var saldoAwal by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {
        fetchFinance()
    }

    fun fetchFinance(onSuccess: (List<Finance>) -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            try {
                // Mengambil data dari API (Langkah 7)
                val fetchedData = RetrofitClient.instance.getFinance()
                listFinance = fetchedData
                onSuccess(fetchedData)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    fun tambahFinance(finance: Finance) {
        listFinance = listOf(finance) + listFinance
    }

    fun setSaldo(saldo: Int) {
        saldoAwal = saldo
    }
}
