package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.polito_mad_01.model.Playground

class ShowFreeSlotsViewModel : ViewModel() {
    private var _formData = MutableLiveData<Playground>().apply {  }
    var changing: Boolean = false
    val formData: LiveData<Playground>
        get() = _formData

    fun setObject(obj: Playground) {
        _formData.value = obj
    }
}