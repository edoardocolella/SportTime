package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.UserRepository
import java.net.URI

class ShowProfileViewModel(private val userRepository:  UserRepository): ViewModel() {

    var user = MutableLiveData<User>()
    fun getUser(): LiveData<User> {
        user = userRepository.getUser() as MutableLiveData<User>
        return user
    }

    fun getUserImage(): LiveData<URI?> {
        return userRepository.getProfileImage()
    }
}

class ShowProfileViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}