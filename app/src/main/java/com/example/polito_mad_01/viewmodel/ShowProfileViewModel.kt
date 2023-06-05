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

    fun getUserById(userId: String): LiveData<User> {
        user = userRepository.getUserById(userId) as MutableLiveData<User>
        return user
    }

    fun getUserImage(): LiveData<Uri?> {
        return userRepository.getProfileImage()
    }


    fun getFriends(): LiveData<List<User>> {
        return userRepository.getUserFriends()
    }

    fun addFriend(email: String) : LiveData<String> {
        return userRepository.addFriend(email)
    }

    fun getFriendRequests(): LiveData<List<String>> {
        return userRepository.getRequestsUUID()
    }
    fun getFriendsRequestsNickname(idList: List<String>): LiveData<List<Pair<String,User>>> {
        return userRepository.getFriendsNickname(idList)
    }

    fun acceptRequest(first: String) {
        userRepository.acceptRequest(first)
    }

    fun declineRequest(first: String) {
        userRepository.declineRequest(first)
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