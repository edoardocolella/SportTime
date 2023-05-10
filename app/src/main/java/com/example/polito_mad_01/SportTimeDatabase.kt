package com.example.polito_mad_01

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.polito_mad_01.db.*


@Database(
    entities = [
        Playground::class,
        Slot::class,
        User::class,
        Review::class],
    version = 1,
    exportSchema = true
)
abstract class SportTimeDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: SportTimeDatabase? = null
        fun getDatabase(context: Context): SportTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val i =
                    Room.databaseBuilder(
                        context.applicationContext,
                        SportTimeDatabase::class.java,
                        "sport_time.db"
                    )
                        .createFromAsset("database/sport_time.db")
                        .build()
                INSTANCE = i
                return i
            }
        }
    }

    abstract fun userDao(): UserDao
    abstract fun playgroundDao(): PlaygroundDao
    abstract fun reservationDao(): ReservationDao
    abstract fun reviewDao(): ReviewDao
}