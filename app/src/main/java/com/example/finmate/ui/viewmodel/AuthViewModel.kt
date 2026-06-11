package com.example.finmate.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.finmate.data.AuthRepository
import com.example.finmate.data.local.FinanceDatabase
import com.example.finmate.data.local.PreferenceManager
import com.example.finmate.data.local.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        FinanceDatabase::class.java, "finmate-db"
    ).fallbackToDestructiveMigration().build()
    
    private val repository = AuthRepository(
        db.userDao(),
        PreferenceManager(application)
    )

    val isLoggedIn: StateFlow<Boolean> = repository.isLoggedIn
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val currentUser: StateFlow<UserEntity?> = repository.userId.flatMapLatest { id ->
        if (id == -1) flowOf<UserEntity?>(null)
        else flow { emit(repository.getUser(id)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = repository.login(email, password)
            if (success) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Email atau password salah")
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val success = repository.register(name, email, password)
            if (success) {
                _authState.value = AuthState.Success
            } else {
                _authState.value = AuthState.Error("Email sudah terdaftar")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }
    
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}
