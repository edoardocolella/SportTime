package com.example.polito_mad_01.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.*
import com.google.firebase.firestore.FirebaseFirestore

class ReservationRepository() {
    fun getReservationByUserId(userID : String): LiveData<List<Slot>> {
        val reservations = MutableLiveData<List<Slot>>()
        FirebaseFirestore.getInstance().collection("users")
            .document(userID)
            .addSnapshotListener { r, _ ->
                //r?.toObject(List<Slot>::class.java)
                println("TEST $r")
            }
        return reservations
    }

    fun getSlotsByUserId(userID : String): LiveData<List<Slot>> {
        val list = mutableListOf<Slot>()
        println("TEST TEST TEST")
        FirebaseFirestore.getInstance().collection("reservations")
            .whereEqualTo("user_id", userID)
            .addSnapshotListener { r, _ ->
                r?.forEach() {
                    println("TEST $it")
                    list.add(it.toObject(Slot::class.java))
                }
            }
        return MutableLiveData(list)
    }

}