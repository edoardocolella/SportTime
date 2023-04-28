package com.example.polito_mad_01.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.polito_mad_01.db.SlotWithPlayground
import com.example.polito_mad_01.repositories.ReservationRepository
import kotlinx.coroutines.flow.Flow

class ShowFreeSlotsViewModel(private val repository: ReservationRepository) : ViewModel() {
    fun getFreeSlots(date:String): LiveData<List<SlotWithPlayground>>{
        return repository.getFreeSlots(date).asLiveData()
    }
}

class ShowFreeSlotsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowFreeSlotsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ShowFreeSlotsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}