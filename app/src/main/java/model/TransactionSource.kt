package model

import com.example.finmate.R

object TransactionSource {

    val dummyTransaction = listOf(
        Transaction("Makan Siang", "Makanan", 20000, R.drawable.makan),
        Transaction("Bensin", "Transport", 15000, R.drawable.transport),
        Transaction("Nonton", "Hiburan", 30000, R.drawable.hiburan),
//        Transaction("Tabungan", "Saving", 50000, R.drawable.tabungan)
    )
}