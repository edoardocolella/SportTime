package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowOldReservationsViewModel(private val repository: ReservationRepository) : ViewModel() {
    fun getOldReservations(u_id: Int, date: String): LiveData<List<SlotWithPlayground>> {
        return repository.getOldReservationsByUserId(u_id, date).asLiveData()
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