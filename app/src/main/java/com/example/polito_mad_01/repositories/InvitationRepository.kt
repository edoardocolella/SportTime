package com.example.polito_mad_01.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.Invitation
import com.example.polito_mad_01.model.InvitationInfo
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore

class InvitationRepository {
    private val fs = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    fun getUserInvitations() : LiveData<List<Invitation>> {
        val invitationList = MutableLiveData<List<Invitation>>().apply { value = listOf() }

        fs.collection("gameRequests")
            .where(
                Filter.equalTo("receiver", userId)
            )
            .get()
            .addOnSuccessListener { result ->
                val requests = result.documents
                    .map { it.toObject(InvitationInfo::class.java)!! }

                requests.forEach {
                    fs.collection("reservations")
                        .document(it.slotID.toString().padStart(3, '0'))
                        .get()
                        .addOnSuccessListener { slot ->
                            val requestSlot = slot.toObject(Slot::class.java)!!

                            fs.collection("users")
                                .document(it.sender)
                                .get()
                                .addOnSuccessListener { user ->
                                    val requestUser = user.toObject(User::class.java)!!

                                    val invitation = Invitation(
                                        requestUser,
                                        requestSlot,
                                    )

                                    invitationList.value = invitationList.value?.plus(
                                        invitation
                                    )
                                }
                        }
                }

            }

        return invitationList
    }
}