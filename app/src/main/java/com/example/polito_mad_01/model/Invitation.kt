package com.example.polito_mad_01.model

data class InvitationInfo(
    val receiver: String,
    val sender: String,
    val slotID: Int,
) {
    constructor() : this("", "", 0,)
}

data class Invitation(
    val sender: User,
    val slot: Slot,
)