package com.adr.calendar.com.adr.calendar.dbLocal

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EventTable::class], version = 1)

abstract class EventTableDatabase : RoomDatabase() {

    abstract fun getEventTableDao() : EventTableDao

    companion object{
        @Volatile private var instance : EventTableDatabase? = null

        private  val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EventTableDatabase::class.java,
            "eventTableDatabase")
            .build()
//            .addMigrations(MIGRATION_1_2)
    }
}