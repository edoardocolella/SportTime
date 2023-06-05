package com.example.polito_mad_01.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.polito_mad_01.model.Invitation
import com.example.polito_mad_01.model.InvitationInfo
import com.example.polito_mad_01.model.Slot
import com.example.polito_mad_01.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import java.time.LocalDateTime

class InvitationRepository {
    private val fs = FirebaseFirestore.getInstance()
    private val fAuth = FirebaseAuth.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUserInvitations() : LiveData<List<InvitationInfo>> {
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")

        val invitationList = MutableLiveData<List<Invitation>>().apply { value = listOf() }
        val invitationInfoList = MutableLiveData<List<InvitationInfo>>().apply { value = listOf() }

        fs.collection("gameRequests")
            .whereEqualTo("receiver", userID)
            .addSnapshotListener{ result, _ ->
                val requests = result!!.documents
                    .filter {
                        (it.getField<String>("date")!! > LocalDateTime.now().toString()) ||
                                (it.getField<String>("date")!! == LocalDateTime.now().toString()
                                        && it.getField<String>("start_time")!! >= LocalDateTime.now().toString())
                    }
                    .map { it.toObject(InvitationInfo::class.java)!! }

                invitationInfoList.value = requests
            }

        return invitationInfoList
    }

    fun getInvitationData(invitation : InvitationInfo) : MutableLiveData<Invitation>{
        val invitationData = MutableLiveData<Invitation>()

        fs.collection("reservations")
            .document(invitation.slotID.toString().padStart(3, '0'))
            .get()
            .addOnSuccessListener { slot ->
                val requestSlot = slot.toObject(Slot::class.java)!!

                fs.collection("users")
                    .document(invitation.sender)
                    .get()
                    .addOnSuccessListener { user ->
                        val requestUser = user.toObject(User::class.java)!!

                        invitationData.value = Invitation(
                            invitation,
                            requestUser,
                            requestSlot,
                        )


                    }
            }

        return invitationData
    }


    fun acceptInvitation(invitation: InvitationInfo){
        val userID = fAuth.currentUser?.uid ?: throw Exception("User not logged in")

        fs.collection("reservations")
            .document(invitation.slotID.toString().padStart(3, '0'))
            .update("attendants", FieldValue.arrayUnion(userID))
            .addOnSuccessListener {
                fs.collection("gameRequests")
                    .document("${invitation.sender}-${invitation.receiver}-${invitation.slotID}")
                    .delete()
            }
    }

    fun declineInvitation(invitation: InvitationInfo){
        fs.collection("gameRequests")
            .document("${invitation.sender}-${invitation.receiver}-${invitation.slotID}")
            .delete()
    }


}