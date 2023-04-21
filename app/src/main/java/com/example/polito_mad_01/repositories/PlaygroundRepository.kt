package com.example.polito_mad_01.repositories

import com.example.polito_mad_01.daos.PlaygroundDao
import com.example.polito_mad_01.model.Playground
import kotlinx.coroutines.flow.Flow

class PlaygroundRepository(private val playgroundDao: PlaygroundDao) {

    val playgrounds: Flow<List<Playground>> = playgroundDao.getAllPlaygrounds()

}