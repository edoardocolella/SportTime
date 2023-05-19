package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowOldReservationsViewModel(private val repository: ReservationRepository) : ViewModel() {
    fun getOldReservations(u_id: Int, date: String): LiveData<List<Slot>> {
        //return repository.getOldReservationsByUserId(u_id, date).asLiveData()
        return MutableLiveData<List<Slot>>()
    }
}

class ShowOldReservationsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T{
        if (modelClass.isAssignableFrom(ShowOldReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowOldReservationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}