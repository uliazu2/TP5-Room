package com.example.userapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.userapp.data.database.AppDatabase
import com.example.userapp.data.entity.User
import com.example.userapp.data.repository.UserRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository
    val allUsers: LiveData<List<User>>

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUsers = repository.allUsers
    }

    fun getUserById(userId: Int): LiveData<User> {
        return repository.getUserById(userId)
    }

    fun insert(user: User, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.insert(user)
            withContext(Dispatchers.Main) {
                onResult(id)
            }
        }
    }

    fun update(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            repository.update(user)
            withContext(Dispatchers.Main) {
                onResult()
            }
        }
    }

    fun delete(user: User, onResult: () -> Unit) {
        viewModelScope.launch {
            repository.delete(user)
            withContext(Dispatchers.Main) {
                onResult()
            }
        }
    }

    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            withContext(Dispatchers.Main) {
                onResult(user)
            }
        }
    }

    fun checkEmailExists(email: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            withContext(Dispatchers.Main) {
                onResult(user != null)
            }
        }
    }

    fun getUserByIdSync(userId: Int, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserByIdSync(userId)
            withContext(Dispatchers.Main) {
                onResult(user)
            }
        }
    }
}
