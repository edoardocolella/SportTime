package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditReservationViewModel: ViewModel() {
    var changing: Boolean = false
    var extraToolsRequired = MutableLiveData<Boolean>()
}