package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.db.Slot
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.repositories.ReservationRepository

class ShowReservationsViewModel(private val reservationsRepository: ReservationRepository) : ViewModel() {

    lateinit var slot : LiveData<SlotWithPlayground>
    fun getReservation(slotID: Int) : LiveData<SlotWithPlayground> {
        slot = reservationsRepository.getReservationById(slotID).asLiveData()

        return slot
    }
}

class ShowReservationsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowReservationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}