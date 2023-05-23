package com.example.polito_mad_01.viewmodel

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

    fun setActualTime(date: String, start: String, end: String) {
        reservation.value?.date = date
        reservation.value?.start_time = start
        reservation.value?.end_time = end
    }

    fun getReservation(id: Int): LiveData<Slot> {
        reservation = repository.getReservationById(id)
        return reservation
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSlotsByPlayground(playgroundID: Int): LiveData<List<Slot>> {
        return MutableLiveData<List<Slot>>()
    }

    fun updateReservation(slot: Slot) {
        thread {
            //repository.updateReservation(slot)
        }
    }

    fun deleteReservation() {
        /*thread {
            val slot = reservation.value?.slot!!
            slot.user_id = null
            slot.heating = false
            slot.lighting = false
            slot.locker_room = false
            slot.equipment = false
            repository.updateReservation(slot)
        }
         */

    }

    fun getSlotByStartEndTimeDatePlayground(
        startTime: String,
        endTime: String,
        date: String,
        playgroundID: Int
    ): LiveData<Slot> {
        //return repository.getSlotByStartEndTime(startTime, endTime,date, playgroundID).asLiveData()
        return MutableLiveData<Slot>()
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