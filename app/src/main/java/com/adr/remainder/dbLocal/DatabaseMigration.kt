package com.adr.remainder.dbLocal

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 : Migration = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL("UPDATE eventtable " + " SET request_code INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE eventtable" + " ADD request_code_id INTEGER NOT NULL DEFAULT 0")
    }
}

val MIGRATION_2_3 : Migration = object : Migration(2,3){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE eventtable_backup (date TEXT NOT NULL, month TEXT NOT NULL, year TEXT NOT NULL, event_name TEXT NOT NULL, hour TEXT NOT NULL, minute TEXT NOT NULL, request_code_id INTEGER NOT NULL, id INTEGER NOT NULL, PRIMARY KEY(id))")
//        database.execSQL("INSERT INTO eventtable_backup (date, month, year, event_name, hour, minute, request_code_id, id) SELECT date, month, year, event_name,hour, minute, request_code_id, id FROM eventtable")
        database.execSQL("DROP TABLE eventtable")
        database.execSQL("ALTER TABLE eventtable_backup RENAME TO eventtable")
//        database.execSQL("ALTER TABLE eventtable" + " DROP COLUMN request_code")
    }
}