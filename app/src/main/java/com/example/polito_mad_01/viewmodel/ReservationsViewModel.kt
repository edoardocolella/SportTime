package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ReservationsViewModel(private val reservationsRepository: ReservationRepository) : ViewModel() {

   fun getUserReservations(userID: String) : LiveData<List<Slot>> {
        //return reservationsRepository.getReservationByUserId(userID)
        return MutableLiveData<List<Slot>>()
    }

    fun getUserSlots(userID: String) : LiveData<List<Slot>> {
        return reservationsRepository.getSlotsByUserId(userID)
    }

    fun getAllReservations() : LiveData<List<Slot>>{
        //return reservationsRepository.getAllReservations().asLiveData()
        return MutableLiveData<List<Slot>>()
    }
}

class ReservationsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}