package com.example.polito_mad_01.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.polito_mad_01.model.SportSkill

@Dao
interface SkillDao {

    @Insert(entity = SportSkill::class, onConflict = OnConflictStrategy.ABORT)
    fun addSkill(skill: SportSkill) {}

}