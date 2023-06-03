package com.example.polito_mad_01.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.example.polito_mad_01.repositories.ReservationRepository
import kotlin.concurrent.thread
import kotlin.random.Random

class ReservationsViewModel(private val reservationsRepository: ReservationRepository) : ViewModel() {

    lateinit var reservations: LiveData<List<Slot>>

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserSlots(): LiveData<List<Slot>> {
        reservations = reservationsRepository.getSlotsByUserId()
        return reservations
    }

   fun createSlots() {
            var index = 1
            var i = 0
            val hourList = listOf(
                Pair("10:00", "11:00"),
                Pair("12:00", "14:00"),
                Pair("15:00", "16:00"),
                Pair("20:00", "21:00")
            )
            while (i< 30) {
                val day = String.format("%02d", (i) % 30 + 1)
                val month = String.format("%02d", (i) / 30 + 6)
                val date = "2023-$month-$day"
                index = add4Slots(date, index, hourList.shuffled())
                i++
            }

        }


    private fun add4Slots(date: String, index: Int, myHourList: List<Pair<String, String>>): Int{
        val serviceMap = mutableMapOf(Pair("heating", false), Pair("equipment", false), Pair("locker_room", false), Pair("lighting", false))

        val slot1 =  Slot(index, null, date, myHourList[0].first, myHourList[0].second, Random.nextInt(1,10), false, serviceMap, "Mana Beach", "Volley", 1, "Turin", mutableListOf(), 8)
        reservationsRepository.populateSlot(slot1)

        val slot2 =  Slot(index+1, null, date, myHourList[1].first, myHourList[1].second, Random.nextInt(1,10), false, serviceMap,   "Centro sociale Comala", "Ping Pong", 2, "Turin", mutableListOf(), 4)
        reservationsRepository.populateSlot(slot2)

        val slot3= Slot(index+2, null, date, myHourList[2].first, myHourList[2].second, Random.nextInt(1,10), false, serviceMap, "Campo da Canestro Braccini", "Basket", 3,"Turin", mutableListOf(), 10)
        reservationsRepository.populateSlot(slot3)

        val slot4 =  Slot(index+3, null, date, myHourList[3].first, myHourList[3].second, Random.nextInt(1,10), false, serviceMap, "Campo Sportivo Carmagnola", "Football", 4, "Turin", mutableListOf(), 10)
        reservationsRepository.populateSlot(slot4)
        return index+4
    }

}

class ReservationsViewModelFactory(private val repository: ReservationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReservationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReservationsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}