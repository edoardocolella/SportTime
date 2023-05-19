package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowFreeSlotsViewModel(private val repository: ReservationRepository) : ViewModel() {
    fun getFreeSlots(date:String): LiveData<List<Slot>>{
        return MutableLiveData<List<Slot>>()
    }
}

class ShowFreeSlotsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowFreeSlotsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowFreeSlotsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}