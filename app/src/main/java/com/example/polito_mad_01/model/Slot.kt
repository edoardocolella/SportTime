package com.example.polito_mad_01.model

data class Slot(
    var slot_id: Int = 0,
    val playground_id: Int,
    var user_id: String?,
    var date: String,
    var start_time: String,
    var end_time: String,
    val total_price: Double,
    var playground: Playground,
    val reserved: Boolean,
    var services: MutableMap<String,Boolean>
    ){
    constructor() : this(0,0,"","","","",0.0, Playground(),false, mutableMapOf())
}


