package com.adr.remainder.dbLocal

import androidx.room.*

@Dao
interface EventTableDao {
    @Insert
    suspend fun addData(eventTable: EventTable)

    @Query("SELECT * FROM eventtable ORDER BY request_code_id DESC")
    suspend fun getAllData() : List<EventTable>

    @Query("SELECT request_code_id FROM eventtable")
    suspend fun getAllRequestCode() : List<Int>

    @Insert
    suspend fun addAllData(vararg eventTable: EventTable)

    @Update
    suspend fun updateData(eventTable: EventTable)

    @Delete
    suspend fun deleteData(eventTable: EventTable)
}