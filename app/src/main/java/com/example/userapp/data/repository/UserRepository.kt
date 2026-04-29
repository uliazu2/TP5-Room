package com.example.userapp.data.repository

import androidx.lifecycle.LiveData
import com.example.userapp.data.dao.UserDao
import com.example.userapp.data.entity.User

class UserRepository(private val userDao: UserDao) {

    val allUsers: LiveData<List<User>> = userDao.getAllUsers()

    suspend fun insert(user: User): Long {
        return userDao.insert(user)
    }

    suspend fun update(user: User) {
        userDao.update(user)
    }

    suspend fun delete(user: User) {
        userDao.delete(user)
    }

    fun getUserById(userId: Int): LiveData<User> {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByIdSync(userId: Int): User? {
        return userDao.getUserByIdSync(userId)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }
}
