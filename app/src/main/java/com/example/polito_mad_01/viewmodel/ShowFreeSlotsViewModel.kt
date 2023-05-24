package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowFreeSlotsViewModel(private val repository: ReservationRepository) : ViewModel() {

    lateinit var freeSlots: LiveData<List<Slot>>

    fun getFreeSlots(date:String): LiveData<List<Slot>>{
        freeSlots = repository.getFutureFreeSlots(date)
        return freeSlots
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