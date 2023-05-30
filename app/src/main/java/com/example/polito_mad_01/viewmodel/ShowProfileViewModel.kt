package com.example.polito_mad_01.viewmodel

import android.net.Uri
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

    fun getUserImage(): LiveData<Uri?> {
        return userRepository.getProfileImage()
    }


    fun getFriends(idList: List<String>): LiveData<List<String>> {
        return userRepository.getFriendsNickname(idList)
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