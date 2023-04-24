package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.db.User
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserWithSkills(id: Int): LiveData<User> = userRepository.userById(id).asLiveData()
    var user: MutableLiveData<User> = MutableLiveData()

    fun updateUser() {
        thread {
            user.value?.let { userRepository.updateUser(it) }
        }
    }
}


class EditProfileViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}