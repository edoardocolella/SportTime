package com.example.polito_mad_01.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.*
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.repositories.UserRepository

class ShowUserProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    var user = MutableLiveData<User>()

    fun getUserById(id: String): LiveData<User> {
        user = userRepository.getUserById(id) as MutableLiveData<User>
        return user
    }

    fun getUserImage(id: String): LiveData<Uri?> {
        return userRepository.getProfileImageById(id)
    }

    fun removeFriend(friendUser: String) {
        userRepository.removeFriend(friendUser)
    }

    fun addFriend(friendUserId: String) {
        userRepository.addFriend(friendUserId)
    }

    fun declineRequest(friendUserId: String) {
        userRepository.declineRequest(friendUserId)
    }

    fun acceptRequest(friendUserId: String) {
        userRepository.acceptRequest(friendUserId)
    }

}

class ShowUserProfileViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowUserProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowUserProfileViewModel( userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}