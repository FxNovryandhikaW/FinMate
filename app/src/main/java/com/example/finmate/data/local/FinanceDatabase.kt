package com.example.finmate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FinanceEntity::class, UserEntity::class], version = 2, exportSchema = false)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun financeDao(): FinanceDao
    abstract fun userDao(): UserDao
}
