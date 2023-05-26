package com.example.polito_mad_01.repositories

import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.google.firebase.firestore.*

class ReservationRepository{
    private val fs = FirebaseFirestore.getInstance()

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
                }

            }
        return liveDataList
    }

    fun getReservationById(slotID: Int): LiveData<Slot> {
        val slot = MutableLiveData<Slot>()
        val idFormatted = slotID.toString().padStart(3, '0')
        fs.collection("reservations")
            .document(idFormatted)
            .addSnapshotListener { r, _ ->
                slot.value = r?.toObject(Slot::class.java)
            }
        return slot
    }

    fun getFutureFreeSlots(date: String): LiveData<List<Slot>> {
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

    fun getOldReservationsByUserId(u_id: String, date: String): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        fs.collection("reservations")
            .where(Filter.and(
                Filter.equalTo("user_id", u_id),
                Filter.lessThan("date", date),
                Filter.equalTo("reserved", true)))
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