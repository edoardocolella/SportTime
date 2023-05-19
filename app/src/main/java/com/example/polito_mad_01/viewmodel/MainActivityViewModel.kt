package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.repositories.*

class MainActivityViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getFirebaseUser(userID: String): LiveData<User> {
        //return userRepository.firebaseUser(userID)
        return MutableLiveData<User>()
    }

    /*
    fun getUser(userID: Int): LiveData<User> {
        return userRepository.userById(userID).asLiveData()
    }
     */
}

class MainActivityViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}