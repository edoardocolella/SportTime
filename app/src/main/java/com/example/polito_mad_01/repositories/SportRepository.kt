package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.db.SportDao
import com.example.polito_mad_01.db.Sport
import kotlinx.coroutines.flow.Flow

class SportRepository(private val sportDao: SportDao) {

    val sports: Flow<List<Sport>> = sportDao.getAllSports()

}