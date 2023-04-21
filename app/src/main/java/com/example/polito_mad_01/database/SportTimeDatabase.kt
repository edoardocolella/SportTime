package com.example.polito_mad_01.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.polito_mad_01.model.Reservation

@Database(entities = [Reservation::class], version = 1)
abstract class SportTimeDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: SportTimeDatabase? = null

        fun getDatabase(context: Context): SportTimeDatabase =
            (
                    instance ?:
                    synchronized(this){
                        val i = instance ?: Room.databaseBuilder(
                            context.applicationContext,
                            SportTimeDatabase::class.java,
                            "sport_time"
                        ).build()
                        instance = i
                        instance
                    }
            )!!
    }


}