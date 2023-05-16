package com.example.polito_mad_01.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.polito_mad_01.db.*
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    lateinit var user : MutableLiveData<UserWithSkills>
    lateinit var chipGroup : LiveData<View>
     var imageUri = MutableLiveData<String>(null)

    fun getUser(userId: Int): LiveData<UserWithSkills>{
        user = userRepository.userWithSkillsById(userId).asLiveData() as MutableLiveData<UserWithSkills>
        return user
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