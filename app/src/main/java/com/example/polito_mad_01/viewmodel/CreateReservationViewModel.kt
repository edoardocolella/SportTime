package com.example.polito_mad_01.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.*
import kotlin.concurrent.thread

class CreateReservationViewModel(private val reservationRepository: ReservationRepository) : ViewModel() {

    lateinit var reservation : MutableLiveData<Slot>


    fun getReservation(slotID: Int): LiveData<Slot> {
        reservation = reservationRepository.getReservationById(slotID) as MutableLiveData<Slot>
        return reservation
    }

    fun createReservation() {
        thread {
            reservationRepository.createOrUpdateReservation(reservation.value!!)
        }
    }

    fun getPlaygroundImage(playgroundId: Int): LiveData<Uri?> {
        return reservationRepository.getSportImage(playgroundId)
    }


}

class CreateReservationViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CreateReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}