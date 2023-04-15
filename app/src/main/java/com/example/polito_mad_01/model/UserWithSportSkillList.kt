package com.example.polito_mad_01.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithSportSkillList (
    @Embedded val user: User,
    @Relation(
        parentColumn = "id",
        entityColumn = "user_id"
    )
    val sportAvailabilityList: List<SportSkill>)
