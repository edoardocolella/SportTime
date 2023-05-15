package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.db.*
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    var user: LiveData<UserWithSkills> = MutableLiveData()
    var newSkill = MutableLiveData<Skill>()


    private val loaded = MutableLiveData(false)

    fun getUser(userId: Int): LiveData<UserWithSkills>{
        if (loaded.value == false) {
            user = userRepository.userWithSkillsById(userId).asLiveData()
            loaded.value = true
            return user;
        }
        return user;
    }

    fun updateUser() {
        thread {
            user.value?.let { userRepository.updateUserWithSkills(it) }
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