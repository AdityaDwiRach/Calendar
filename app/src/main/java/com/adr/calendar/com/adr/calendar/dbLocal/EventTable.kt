package com.adr.calendar.com.adr.calendar.dbLocal

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "eventtable")
data class EventTable(
    val date: String,
    val month: String,
    val year: String,
    val event_name: String,
    val hour: String,
    val minute: String,
    val time_full: String
):Serializable{
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0
}