package com.example.finmate.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.finmate.model.Finance
import com.example.finmate.model.FinanceSource

class FinanceViewModel : ViewModel() {

    var listFinance by mutableStateOf(FinanceSource.dummyFinance)
        private set

    var saldoAwal by mutableStateOf(0)
        private set

    fun tambahFinance(finance: Finance) {
        listFinance = listOf(finance) + listFinance
    }

    fun setSaldo(saldo: Int) {
        saldoAwal = saldo
    }
}