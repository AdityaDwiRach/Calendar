package com.adr.calendar.com.adr.calendar.dbLocal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class SQLiteDatabseHandler (ctx: Context) : ManagedSQLiteOpenHelper(ctx, "EventTable.db", null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(EventTable.EVENT_TABLE, true,
            EventTable.ID to TEXT + PRIMARY_KEY,
            EventTable.DATE to TEXT,
            EventTable.MONTH to TEXT,
            EventTable.YEAR to TEXT,
            EventTable.EVENT_NAME to TEXT,
            EventTable.HOUR to TEXT,
            EventTable.MINUTE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(EventTable.EVENT_NAME, true)
    }

    companion object{
        private var instance: SQLiteDatabseHandler? = null

        @Synchronized
        fun getInstance(ctx: Context) : SQLiteDatabseHandler {
            if (instance == null){
                instance = SQLiteDatabseHandler(ctx.applicationContext)
            }
            return instance as SQLiteDatabseHandler
        }
    }
}

val Context.database : SQLiteDatabseHandler
get() = SQLiteDatabseHandler.getInstance(applicationContext)