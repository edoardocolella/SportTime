package com.example.polito_mad_01.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.polito_mad_01.model.Invitation
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.repositories.InvitationRepository
import com.example.polito_mad_01.repositories.ReservationRepository
import com.example.polito_mad_01.repositories.UserRepository

class InvitationsViewModel( private val invitationRepository: InvitationRepository,
                            private val reservationRepository: ReservationRepository)
    : ViewModel(){

        @RequiresApi(Build.VERSION_CODES.O)
        fun getReservations() : LiveData<List<Slot>>{
            return reservationRepository.getReservationsByUserId()
        }

        fun getUserInvitations() : LiveData<List<Invitation>> {
            return invitationRepository.getUserInvitations()
        }
}

class InvitationsViewModelFactory(private val invitationRepository: InvitationRepository, private val reservationRepository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InvitationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InvitationsViewModel(invitationRepository, reservationRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}