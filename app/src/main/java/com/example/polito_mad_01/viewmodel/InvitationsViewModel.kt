package com.example.polito_mad_01.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.repositories.ReservationRepository

class InvitationsViewModel(private val reservationRepository: ReservationRepository)
    : ViewModel(){

        @RequiresApi(Build.VERSION_CODES.O)
        fun getReservations() : LiveData<List<Slot>>{
            return reservationRepository.getReservationsByUserId()
        }
}

class InvitationsViewModelFactory(private val reservationRepository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvitationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvitationsViewModel(reservationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}