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


    private class SportTimeDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val userDao = database.userDao()
                    // Add sample words.
                    val user = User(
                        0, "test", "test", "test", "test",
                        "test", "test", "test", "test", "test",
                        "test", "test", "test", "test",
                        "test", "test", "test", "test"
                    )
                    userDao.addUser(user)
                }
            }
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
                ).addCallback(SportTimeDatabaseCallback(scope))
                    .build()
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