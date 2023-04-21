package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.daos.SportDao
import com.example.polito_mad_01.model.Sport
import kotlinx.coroutines.flow.Flow

class SportRepository(private val sportDao: SportDao) {

    val sports: Flow<List<Sport>> = sportDao.getAllSports()

}