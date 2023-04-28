package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.db.Reservation
import com.example.polito_mad_01.repositories.ReservationRepository
import com.example.polito_mad_01.repositories.UserRepository

class ReservationsViewModel(private val reservationsRepository: ReservationRepository) : ViewModel() {
    fun getAllReservations() : LiveData<List<Reservation>>{
        return reservationsRepository.reservations.asLiveData()
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