package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.UserWithSkills
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUserWithSkills(id: Int): LiveData<UserWithSkills> {
        return userRepository.userWithSkillsById(id).asLiveData()
    }

    var user: MutableLiveData<UserWithSkills> = MutableLiveData()
    val expertList: MutableLiveData<String> = MutableLiveData()
    val intermediateList: MutableLiveData<String> = MutableLiveData()
    val beginnerList: MutableLiveData<String> = MutableLiveData()


    fun updateUser() {
        user.value?.let {
            thread {
                userRepository.updateUser(it.user)
                userRepository.deleteSkills(it.user.userId)
                userRepository.insertAllSkills(it.skills)
            }
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