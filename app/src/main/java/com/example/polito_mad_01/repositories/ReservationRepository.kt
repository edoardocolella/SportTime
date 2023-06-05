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

class ReservationRepository{
    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSlotsByUserId(): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("reservations")
            .where(Filter.or(
                    Filter.arrayContains("attendants", userID),
                Filter.equalTo("reserved", false)
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

    fun getReservationsByUserId(): LiveData<List<Slot>> {
        val liveDataList = MutableLiveData<List<Slot>>()
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")
        fs.collection("reservations")
            .where(Filter.and(
                Filter.arrayContains("attendants", userID),
                Filter.equalTo("reserved", true)
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

    fun getReservationParticipants(slotID: Int) : LiveData<List<Pair<User,String>>>{
        val idFormatted = slotID.toString().padStart(3, '0')
        val reservationQuery = fs.collection("reservations").document(idFormatted)

        val liveDataList = MutableLiveData<List<Pair<User,String>>>()

        reservationQuery.addSnapshotListener { r, _ ->
            val attendants = r?.toObject(Slot::class.java)?.attendants

            if(attendants != null && attendants.isNotEmpty()){
                val usersQuery = fs.collection("users").get()

                usersQuery.addOnSuccessListener { query ->
                    val list = query.documents.filter { attendants.contains(it.id) }.map {
                        Pair(it.toObject(User::class.java)!!, it.id)
                    }
                    liveDataList.value = list
                }
            } else {
                println("VUOTO")
            }

        }

        return liveDataList
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
    }

    fun createOrUpdateReservation(slot: Slot) {
        val userID = fAuth.currentUser?.uid ?: throw Exception("No user found")
        slot.user_id = userID
        slot.reserved = true
        slot.attendants.add(userID)
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
    }

    fun deleteReservation(slot: Slot){
        slot.user_id = null
        slot.reserved = false
        slot.services.forEach{
            slot.services[it.key] = false
        }
        slot.attendants.clear()
        fs.collection("reservations")
            .document(String.format("%03d",slot.slot_id))
            .set(slot, SetOptions.merge())
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
            image.value = null
        }
        return image
    }

}