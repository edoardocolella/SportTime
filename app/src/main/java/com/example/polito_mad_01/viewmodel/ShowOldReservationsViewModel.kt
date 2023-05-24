package com.example.polito_mad_01.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowOldReservationsViewModel(private val repository: ReservationRepository) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getOldReservations(u_id: String, date: String): LiveData<List<Slot>> {
        return repository.getOldReservationsByUserId(u_id, date)
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