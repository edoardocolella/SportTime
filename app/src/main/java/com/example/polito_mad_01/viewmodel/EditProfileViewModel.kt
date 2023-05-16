package com.example.polito_mad_01.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.example.polito_mad_01.db.*
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    var user : LiveData<UserWithSkills> = MutableLiveData()
    lateinit var chipGroup : LiveData<View>

    private val loaded = MutableLiveData(false)

     var imageUri = MutableLiveData<String>(null)

    fun getUser(userId: Int): LiveData<UserWithSkills>{
        if (loaded.value == false) {
            user = userRepository.userWithSkillsById(userId)
                .asLiveData() as MutableLiveData<UserWithSkills>
            loaded.value = true
            return user;
        }
        return user
    }

    fun updateUser() {
        println("TEST UPDATE USER ${user.value}")
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