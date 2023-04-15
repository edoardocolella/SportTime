package com.example.polito_mad_01.model

import androidx.room.Entity

@Entity(tableName = "sport_skill")
data class SportSkill (
    val id: Int,
    val user_id: Int,
    val sport: String,
    val skill: String
    )
