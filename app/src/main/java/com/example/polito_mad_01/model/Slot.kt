package com.example.polito_mad_01.model

data class Slot(
    var slot_id: Int = 0,
    var user_id: String?,
    var date: String,
    var start_time: String,
    var end_time: String,
    val total_price: Double,
    var reserved: Boolean,
    var services: MutableMap<String,Boolean>,
    val playgroundName: String,
    val sport: String,
    val playground_id: Int,
    val location: String,
    val attendants: List<String>,
    val maxNumber: Int
){
    constructor(): this(0, "", "", "", "", 0.0, false, mutableMapOf(), "", "", 0, "", listOf(), 0)
}



