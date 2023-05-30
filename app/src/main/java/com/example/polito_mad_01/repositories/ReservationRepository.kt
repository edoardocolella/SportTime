package com.example.polito_mad_01.repositories

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.example.polito_mad_01.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.time.LocalDate
import java.time.LocalTime

class ReservationRepository{
    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSlotsByUserId(): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        val userID = fAuth.currentUser?.uid ?: ""
        fs.collection("reservations")
            .where(Filter.or(
                    Filter.equalTo("user_id", userID),
                    Filter.and(
                        Filter.equalTo("reserved", false),
                        Filter.greaterThanOrEqualTo("date", LocalDate.now().toString())
                        //,Filter.greaterThan("start_time", LocalTime.now().toString())
                        )
                    )
            )
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
                Filter.greaterThanOrEqualTo("date", date),
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

    fun populateSlot(slot: Slot){
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
            //.addOnSuccessListener { println("$slot created") }
    }

    fun createOrUpdateReservation(slot: Slot) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("No user found")
        slot.user_id = userID
        slot.reserved = true
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
            //.addOnSuccessListener { println("$slot created") }
    }

    fun deleteReservation(slot: Slot){
        slot.user_id = null
        slot.reserved = false
        slot.services.forEach{
            slot.services[it.key] = false
        }
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
            //.addOnSuccessListener { println("$slot created") }
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

    fun getOldReservationsByUserId(date: String): LiveData<List<Slot>> {
        val userID = fAuth.currentUser?.uid ?: ""
        val liveDataList = MutableLiveData<List<Slot>>()
        fs.collection("reservations")
            .where(Filter.and(
                Filter.equalTo("user_id", userID),
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

    fun getSportImage(playgroundId: Int): LiveData<Uri?> {
        val storageReference = storage.reference
        val imageRef = storageReference.child("playgroundImages/$playgroundId.jpg")
        val localFile = File.createTempFile("images", "jpg")
        val image = MutableLiveData<Uri?>()
        imageRef.getFile(localFile).addOnSuccessListener {
            image.value = localFile.toUri()
        }.addOnFailureListener {
            //println("Error while downloading image")
            image.value = null
        }
        return image
    }

}