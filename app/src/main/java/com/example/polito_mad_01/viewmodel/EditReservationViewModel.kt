package com.example.polito_mad_01.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.polito_mad_01.db.*
import com.example.polito_mad_01.repositories.ReservationRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class EditReservationViewModel(private val repository: ReservationRepository): ViewModel() {

    var reservation: LiveData<SlotWithPlayground> = MutableLiveData()
    fun getReservation(id: Int): LiveData<SlotWithPlayground> {
        reservation = repository.getReservationById(id).asLiveData()
        return reservation
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSlotsByPlayground(playgroundID: Int, ) : LiveData<List<SlotWithPlayground>> {
        return repository.getFreeSlotsByPlayground(playgroundID, LocalDate.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd"))).asLiveData()
    }

    fun updateReservation() {
        thread {
            reservation.value?.slot?.let{repository.updateReservation(it)}
        }
    }

    fun deleteReservation() {
        thread {
            val slot = reservation.value?.slot!!
            slot.user_id = null
            slot.heating = false
            slot.lighting = false
            slot.locker_room = false
            slot.equipment = false
            repository.updateReservation(slot)
        }

    }

}

class EditReservationViewModelFactory(private val repository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditReservationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}