package com.example.polito_mad_01.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sport_skill", primaryKeys = ["id", "user_id"])
data class SportSkill (
    val user_id: Int,
    val sport: String,
    val skill: String
    ){
    @PrimaryKey(autoGenerate = true)var id: Int = 0
}