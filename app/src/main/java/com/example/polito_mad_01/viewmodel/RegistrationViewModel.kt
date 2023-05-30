package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.User

class RegistrationViewModel() : ViewModel(){
    lateinit var user : MutableLiveData<User> /*= User("","","","","","", listOf(),
        mutableMapOf<String,String>(),"","","", mutableMapOf(), listOf()
    )*/
    var userId = 0
    lateinit var password : String
}