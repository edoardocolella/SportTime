package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditProfileViewModel: ViewModel() {
    val fullName = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val age = MutableLiveData<String>()
    val gender = MutableLiveData<String>()
    var genderIndex: MutableLiveData<Int> = MutableLiveData<Int>()
    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val location = MutableLiveData<String>()
    val mondayAvailability = MutableLiveData<String>()
    val tuesdayAvailability = MutableLiveData<String>()
    val wednesdayAvailability = MutableLiveData<String>()
    val thursdayAvailability = MutableLiveData<String>()
    val fridayAvailability = MutableLiveData<String>()
    val saturdayAvailability = MutableLiveData<String>()
    val sundayAvailability = MutableLiveData<String>()
    val expertList = MutableLiveData<String>()
    val intermediateList = MutableLiveData<String>()
    val beginnerList = MutableLiveData<String>()
    val imageUriString = MutableLiveData<String>()
    var changing: Boolean = false
}