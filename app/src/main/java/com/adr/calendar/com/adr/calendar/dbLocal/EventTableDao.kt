package com.adr.calendar.com.adr.calendar.dbLocal

import androidx.room.*

@Dao
interface EventTableDao {
    @Insert
    suspend fun addData(eventTable: EventTable)

    @Query("SELECT * FROM eventtable ORDER BY request_code DESC")
    suspend fun getAllData() : List<EventTable>

    @Insert
    suspend fun addAllData(vararg eventTable: EventTable)

    @Update
    suspend fun updateData(eventTable: EventTable)

    @Delete
    suspend fun deleteData(eventTable: EventTable)
}