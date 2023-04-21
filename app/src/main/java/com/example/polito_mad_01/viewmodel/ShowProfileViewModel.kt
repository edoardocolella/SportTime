package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.UserWithSkills

class ShowProfileViewModel: ViewModel() {
    val user = MutableLiveData<UserWithSkills>()
}