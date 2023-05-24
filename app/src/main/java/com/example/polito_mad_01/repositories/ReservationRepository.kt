package com.example.polito_mad_01.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.*
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ReservationRepository(val fs:FirebaseFirestore){

    fun getSlotsByUserId(userID : String): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        fs.collection("reservations")
            .where(Filter.or(
                    Filter.equalTo("user_id", userID),
                    Filter.equalTo("reserved", false)))
            .addSnapshotListener { r, _ ->
                val list = mutableListOf<Slot>()
                r?.forEach {
                    list += it.toObject(Slot::class.java)
                    liveDataList.value = list
                    //println("TEST $it")
                }

            }
        return liveDataList
    }

    fun getReservationById(slotID: Int): LiveData<Slot> {
        val slot = MutableLiveData<Slot>()
        fs.collection("reservations")
            .document(slotID.toString())
            .addSnapshotListener { r, _ ->
                slot.value = r?.toObject(Slot::class.java)
            }
        return slot
    }

    fun getFutureFreeSlots(date: String): LiveData<List<Slot>> {
        //println("DATE $date")
        val liveDataList = MutableLiveData<List<Slot>>()
        fs.collection("reservations")
            .where(Filter.and(
                Filter.greaterThan("date", date),
                Filter.equalTo("reserved", false)))
            .addSnapshotListener { r, _ ->
                val list = mutableListOf<Slot>()
                r?.forEach {
                    list += it.toObject(Slot::class.java)
                    liveDataList.value = list
                    //println("TEST $it")
                }

            }
        return liveDataList
    }

    fun createOrUpdateReservation(slot: Slot) {
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
            .addOnSuccessListener { println("$slot created") }
    }

    fun getAllReservations(): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        fs.collection("reservations")
            .addSnapshotListener { r, _ ->
                val list = mutableListOf<Slot>()
                r?.forEach {
                    list += it.toObject(Slot::class.java)
                    liveDataList.value = list
                }
            }
        return liveDataList
    }


}