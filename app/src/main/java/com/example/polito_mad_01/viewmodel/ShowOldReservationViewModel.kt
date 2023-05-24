package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowOldReservationViewModel(private val repository: ReservationRepository) : ViewModel() {
    lateinit var slot: LiveData<Slot>
    fun getOldReservationById(id: Int): LiveData<Slot> {
        //slot = repository.getOldReservationById(id).asLiveData()
        return slot
    }
}

class ShowOldReservationViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(ShowOldReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowOldReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}