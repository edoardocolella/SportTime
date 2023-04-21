package com.example.polito_mad_01.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.database.UserRepository
import com.example.polito_mad_01.model.User
import kotlin.concurrent.thread

class TestViewModel(private val repo: UserRepository): ViewModel() {

    fun getUser() : String{
        val user = repo.user().value
        return user?.toString() ?: "ERROR"
    }

    fun addUser() {
        thread {
            val user = User(0,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                "test")
        repo.addUser(user)
            print("ADD")
        }
    }

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