package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.model.UserData
import com.example.polito_mad_01.repositories.UserRepository
import kotlin.concurrent.thread

class RegistrationViewModel : ViewModel(){
    fun createUser(uuid: String) {
        thread{
            UserRepository().createUser(user.value!!.toUser(), uuid)

        }
    }

    val user = MutableLiveData<UserData>().apply { value = UserData() }
}