package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.Reservation

class EditReservationViewModel: ViewModel() {
    var changing: Boolean = false
    var extraToolsRequired = MutableLiveData<Boolean>()
}