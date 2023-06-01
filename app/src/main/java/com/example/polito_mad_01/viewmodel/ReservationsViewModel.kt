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

    fun getAllReservations() : LiveData<List<Slot>>{
        return reservationsRepository.getAllReservations()
    }

   fun createSlots() {
            var index = 1
            val userList = listOf("HnA8Ri0zdJfRWZEAbma7eRtWUjW2", null, null, null)
            var i = 0
            val hourList = listOf(
                Pair("10:00", "11:00"),
                Pair("12:00", "14:00"),
                Pair("15:00", "16:00"),
                Pair("20:00", "21:00")
            )
            while (i< 30) {
                val day = String.format("%02d", (i+15) % 30 + 1)
                val month = String.format("%02d", (i+15) / 30 + 5)
                val date = "2023-$month-$day"
                index = add4Slots(date, index, hourList.shuffled(), userList.shuffled() )
                i++
            }

        }


    private fun add4Slots(date: String, index: Int, myHourList: List<Pair<String, String>>, myList: List<String?>): Int{
        val booleanList = listOf(true,false)
        val randomMap = mutableMapOf(Pair("heating", booleanList.random()), Pair("equipment", booleanList.random()), Pair("locker_room", booleanList.random()), Pair("lighting", booleanList.random()))
        val falseMap = mutableMapOf(Pair("heating", false), Pair("equipment", false), Pair("locker_room", false), Pair("lighting", false))

        var map = if(myList[0] != null) randomMap else falseMap
        var attendants = if(myList[0] != null) mutableListOf(myList[0]!!) else mutableListOf()
        val slot1 =  Slot(index, myList[0], date, myHourList[0].first, myHourList[0].second, Random.nextDouble(1.0,10.0), myList[0] != null, map, "Mana Beach", "Volley", 1, "Turin", attendants, 8)
        reservationsRepository.populateSlot(slot1)

        map = if(myList[1] != null) randomMap else falseMap
        attendants = if(myList[1] != null) mutableListOf(myList[1]!!) else mutableListOf()
        val slot2 =  Slot(index+1, myList[1], date, myHourList[1].first, myHourList[1].second, Random.nextDouble(1.0,10.0), myList[1] != null, map,   "Centro sociale Comala", "Ping Pong", 2, "Turin", attendants, 4)
        reservationsRepository.populateSlot(slot2)

        map = if(myList[2] != null) randomMap else falseMap
        attendants = if(myList[2] != null) mutableListOf(myList[2]!!) else mutableListOf()
        val slot3= Slot(index+2, myList[2], date, myHourList[2].first, myHourList[2].second, Random.nextDouble(1.0,10.0), myList[2] != null, map, "Campo da Canestro Braccini", "Basket", 3,"Turin", attendants, 10)
        reservationsRepository.populateSlot(slot3)

        map = if(myList[3] != null) randomMap else falseMap
        attendants = if(myList[3] != null) mutableListOf(myList[3]!!) else mutableListOf()
        val slot4 =  Slot(index+3, myList[3], date, myHourList[3].first, myHourList[3].second, Random.nextDouble(1.0,10.0), myList[3] != null, map, "Campo Sportivo Carmagnola", "Football", 4, "Turin", attendants, 10)
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