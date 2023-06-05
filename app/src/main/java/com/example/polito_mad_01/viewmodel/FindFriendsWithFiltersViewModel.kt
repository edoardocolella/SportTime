package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.repositories.UserRepository

class FindFriendsWithFiltersViewModel (private val userRepository: UserRepository):ViewModel(){


     var skillName = MutableLiveData<String>()
     var skillValue = MutableLiveData<String>()
     var location = MutableLiveData<String>()


    fun findFriendBySkillAndLocation(skillName: String, skillValue:String, location: String): LiveData<List<Pair<User,String>>> {
        return userRepository.findFriendsBySkillAndLocation(skillName, skillValue, location)
    }

    fun sendRequest(email: String){
        userRepository.addFriend(email)
    }
}

class FindFriendsWithFiltersViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FindFriendsWithFiltersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FindFriendsWithFiltersViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}