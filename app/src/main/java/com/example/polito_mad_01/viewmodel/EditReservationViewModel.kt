package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.Reservation

class EditReservationViewModel: ViewModel() {
    private var _formData = MutableLiveData<Reservation>()
    var changing: Boolean = false
    val formData: LiveData<Reservation>
        get() = _formData

    fun setObject(obj: Reservation) {
        _formData.value = obj
    }
}