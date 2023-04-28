package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.*
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.repositories.ReservationRepository

class EditReservationViewModel(private val repository: ReservationRepository): ViewModel() {

    fun getReservation(id: Int): LiveData<SlotWithPlayground> {
        return repository.getReservationById(id).asLiveData()
    }

}

class EditReservationViewModelFactory(private val repository: ReservationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditReservationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}