package com.example.polito_mad_01.model
data class Playground(
    val playground_id: Int = 0,
    val name: String,
    val description: String,
    val location: String,
    val price_per_slot: Double,
    val sport_name: String,
){
    constructor() : this(0, "", "", "", 0.0, "")
}