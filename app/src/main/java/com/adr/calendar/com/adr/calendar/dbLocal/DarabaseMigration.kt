package com.adr.calendar.com.adr.calendar.dbLocal

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 : Migration = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("UPDATE eventtable " + " SET request_code INTEGER NOT NULL DEFAULT 0")
    }
}