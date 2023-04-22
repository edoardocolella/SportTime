package com.example.polito_mad_01

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.polito_mad_01.daos.PlaygroundDao
import com.example.polito_mad_01.daos.ReservationDao
import com.example.polito_mad_01.daos.SportDao
import com.example.polito_mad_01.daos.UserDao
import com.example.polito_mad_01.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Playground::class,
        PlaygroundExtra::class,
        Reservation::class,
        ReservationExtra::class,
        Sport::class,
        SportSkill::class,
        User::class],
    version = 1,
    exportSchema = false
)
abstract class SportTimeDatabase : RoomDatabase() {


    private class SportTimeDatabaseCallback(private val scope: CoroutineScope) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateData(database)
                }
            }
        }

       private suspend fun populateData(database: SportTimeDatabase) {
            populateSport(database)
            populateUser(database)
            populatePlayground(database)
            populateReservation(database)
        }

        private suspend fun populateReservation(database: SportTimeDatabase) {
            val reservationDao = database.reservationDao()
            val reservation1 = Reservation(0, 1, 1, "2021-01-01", "12:00", "14:00", 10.5)
            val reservation2 = Reservation(1, 2, 1, "2021-01-01", "10:00", "12:00", 12.6)
            reservationDao.addReservation(reservation1)
            reservationDao.addReservation(reservation2)
        }

        suspend fun populatePlayground(database: SportTimeDatabase) {
            val playgroundDao = database.playgroundDao()
            val playground1 = Playground(0, "Parco Dora", "Nice :)", "Turin", 10.0, 1)
            val playground2 = Playground(1, "Camp Nou", "Sì, lo stadio", "Barcellona", 100.0, 1)
            playgroundDao.addPlayground(playground1)
            playgroundDao.addPlayground(playground2)
        }

        private suspend fun populateUser(database: SportTimeDatabase) {
            database.userDao().addUser(
                User(
                    1, "Mario", "Rossi", "mar10_r0ss1", "Hey there, I'm using WhatsApp!",
                    "M", "01/01/1990", "Turin", "mario.rossi@mail.com", "1234567890",
                    null, null, "12-14", null,
                    null, null, "18-20", null
                )
            )
        }

        private suspend fun populateSport(database: SportTimeDatabase) {
            val sportDao = database.sportDao()
            sportDao.addSport(Sport(0, "Basket"))
            sportDao.addSport(Sport(1, "Football"))
            sportDao.addSport(Sport(2, "Tennis"))
            sportDao.addSport(Sport(3, "Volley"))
            sportDao.addSport(Sport(4, "Ping Pong"))
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SportTimeDatabase? = null
        fun getDatabase(context: Context, scope: CoroutineScope): SportTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val i = Room.databaseBuilder(
                    context.applicationContext,
                    SportTimeDatabase::class.java,
                    "sport_time"
                ).addCallback(SportTimeDatabaseCallback(scope)).build()
                INSTANCE = i
                return i
            }
        }
    }

    abstract fun userDao(): UserDao
    abstract fun playgroundDao(): PlaygroundDao
    abstract fun sportDao(): SportDao
    abstract fun reservationDao(): ReservationDao
}