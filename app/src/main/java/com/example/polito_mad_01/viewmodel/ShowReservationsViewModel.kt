package com.example.polito_mad_01.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository
import com.example.polito_mad_01.repositories.UserRepository

class ShowReservationsViewModel(private val reservationsRepository: ReservationRepository, val userRepository: UserRepository) : ViewModel() {

    lateinit var slot : LiveData<Slot>
    fun getReservation(slotID: Int) : LiveData<Slot> {
        slot = reservationsRepository.getReservationById(slotID)
        return slot
    }

    fun getPlaygroundImage(playgroundId: Int): LiveData<Uri?> {
        return reservationsRepository.getSportImage(playgroundId)
    }

    fun getReservationParticipants(slotID: Int) : LiveData<List<Pair<User,String>>>{
        return reservationsRepository.getReservationParticipants(slotID)
    }

    fun getUserFriends() : LiveData<List<Pair<User,String>>> {
        return userRepository.getUserFriends()
    }

    fun sendGameRequest(invitedMail: String, slot: Slot) {
        userRepository.sendGameRequest(invitedMail, slot)
    }
}

class ShowReservationsViewModelFactory(private val repository: ReservationRepository, private val userRepository: UserRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowReservationsViewModel(repository, userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}