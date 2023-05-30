package com.example.polito_mad_01.viewmodel

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class EditReservationViewModel(private val repository: ReservationRepository) : ViewModel() {

    lateinit var reservation: LiveData<Slot>
    lateinit var originalStartTime: LiveData<String>
    lateinit var originalEndTime: LiveData<String>
    lateinit var originalDate: LiveData<String>
    fun setOriginalTime(startTime: String, endTime: String, date: String) {
        originalStartTime = MutableLiveData(startTime)
        originalEndTime = MutableLiveData(endTime)
        originalDate = MutableLiveData(date)
    }

    fun getReservation(id: Int): LiveData<Slot> {
        reservation = repository.getReservationById(id)
        return reservation
    }

    fun updateReservation() {
        thread {
            val slot = reservation.value!!
            repository.createOrUpdateReservation(slot)
        }
    }

    fun deleteReservation() {
        thread {
            val slot = reservation.value!!
            repository.deleteReservation(slot)
        }
    }

    fun getPlaygroundImage(playgroundId: Int): LiveData<Uri?> {
        return repository.getSportImage(playgroundId)
    }


}

class EditReservationViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}