package com.example.finmate.data

import com.example.finmate.data.local.UserDao
import com.example.finmate.data.local.UserEntity
import com.example.finmate.data.local.PreferenceManager
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val userDao: UserDao,
    private val preferenceManager: PreferenceManager
) {
    val isLoggedIn: Flow<Boolean> = preferenceManager.isLoggedIn
    val userId: Flow<Int> = preferenceManager.userId

    suspend fun login(email: String, password: String): Boolean {
        val user = userDao.login(email, password)
        return if (user != null) {
            preferenceManager.setUserSession(user.id, true)
            true
        } else {
            false
        }
    }

    suspend fun register(name: String, email: String, password: String): Boolean {
        val existingUser = userDao.getUserByEmail(email)
        return if (existingUser == null) {
            userDao.register(UserEntity(name = name, email = email, password = password))
            true
        } else {
            false
        }
    }

    suspend fun logout() {
        preferenceManager.clearSession()
    }

    suspend fun getUser(id: Int): UserEntity? {
        return userDao.getUserById(id)
    }
}
