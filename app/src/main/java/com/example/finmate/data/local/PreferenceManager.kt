package com.example.finmate.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PreferenceManager(private val context: Context) {
    companion object {
        private val INITIAL_BALANCE_PREFIX = "initial_balance_"
        private val REMINDER_PREFIX = "reminder_"
        val USER_ID = intPreferencesKey("user_id")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    fun getInitialBalance(userId: Int): Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[intPreferencesKey(INITIAL_BALANCE_PREFIX + userId)] ?: 0
    }

    fun getReminder(userId: Int): Flow<String> = context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(REMINDER_PREFIX + userId)] ?: "Bayar kos — tanggal 1 setiap bulan"
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userId: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[USER_ID] ?: -1
    }

    suspend fun setInitialBalance(userId: Int, balance: Int) {
        context.dataStore.edit { preferences ->
            preferences[intPreferencesKey(INITIAL_BALANCE_PREFIX + userId)] = balance
        }
    }

    suspend fun setReminder(userId: Int, reminder: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(REMINDER_PREFIX + userId)] = reminder
        }
    }

    suspend fun setUserSession(id: Int, loggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = id
            preferences[IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = -1
            preferences[IS_LOGGED_IN] = false
        }
    }
}
