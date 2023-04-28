package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.db.Slot

class ShowReservationsViewModel : ViewModel() {
    private var _formData = MutableLiveData<Slot>()
    var changing: Boolean = false
    val formData: LiveData<Slot>
        get() = _formData

    fun setObject(obj: Slot) {
        _formData.value = obj
    }
}