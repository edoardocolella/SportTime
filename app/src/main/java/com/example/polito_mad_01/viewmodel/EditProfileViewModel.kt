package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class EditProfileViewModel: ViewModel() {
    private var _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    var changing: Boolean = false
    val formData: LiveData<JSONObject>
        get() = _formData

    fun addField(key: String, value: Any): EditProfileViewModel {
        _formData.value?.put(key, value); return this
    }

    fun setObject(obj: JSONObject) {
        _formData.value = obj
    }

    fun clearFields() {
        _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    }
}