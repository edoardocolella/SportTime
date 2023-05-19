package com.example.polito_mad_01.model

data class Slot(
    var slot_id: Int = 0,
    val playground_id: Int,
    var user_id: Int?,
    var date: String,
    var start_time: String,
    var end_time: String,
    val total_price: Double,
    var lighting: Boolean,
    var heating: Boolean,
    var equipment: Boolean,
    var locker_room: Boolean,
    var playground: Playground
    )
{
    constructor() : this(0, 0, 0, "", "", "", 0.0, false, false, false, false, Playground())
}


