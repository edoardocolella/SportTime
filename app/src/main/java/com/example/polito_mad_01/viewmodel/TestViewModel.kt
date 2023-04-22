package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.repositories.UserRepository
import com.example.polito_mad_01.model.User
import kotlin.concurrent.thread

class TestViewModel(private val repo: UserRepository) : ViewModel() {

    fun userById(userId: Int): User? {
        val user =  repo.userById(userId).asLiveData()
        return user.value
    }

    val users: LiveData<List<User>> = repo.users.asLiveData()
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}