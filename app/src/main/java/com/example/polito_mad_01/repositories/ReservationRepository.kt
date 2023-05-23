package com.example.polito_mad_01.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.*
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOldReservationsByUserId(user_id: String, date: String): LiveData<List<Slot>>{
        val oldReservations = MutableLiveData<List<Slot>>()
        val l = mutableListOf<Slot>()
        FirebaseFirestore.getInstance().collection("reservations")
            .whereLessThan("date",date).addSnapshotListener { documents, _ ->
                println("----date: $date, size: ${documents?.documents?.size}----")
                documents?.forEach { document ->
                    println("------------${document.toObject(Slot::class.java)}------------")
                    l.add(document.toObject(Slot::class.java))
                }
                oldReservations.value = l
            }
        return oldReservations
    }

}