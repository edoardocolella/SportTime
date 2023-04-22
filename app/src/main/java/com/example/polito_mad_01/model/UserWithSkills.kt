package com.example.polito_mad_01.model

import androidx.room.Embedded
import androidx.room.Relation

class UserWithSkills (
    @Embedded  val user: User,
    @Relation(
        parentColumn = "user_id",
        entityColumn = "user_id"
    )
    var skills: List<SportSkill>
)
{
    override fun toString(): String {
        return "UserWithSkills(user=$user, skills=$skills)"
    }
}