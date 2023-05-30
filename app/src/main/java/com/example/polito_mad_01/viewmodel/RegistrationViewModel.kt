package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.User
import com.example.polito_mad_01.model.UserData

class RegistrationViewModel : ViewModel(){
    val user = MutableLiveData<UserData>().apply { value = UserData() }
}