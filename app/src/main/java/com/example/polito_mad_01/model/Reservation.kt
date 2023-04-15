package com.example.polito_mad_01.model

class Reservation(
    val id: String,
    val playground: Playground,
    val user: User,
    val date: String,
    val startTime: String,
    val endTime: String,
    val price: Double){}