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
            EventTable.EVENT_NAME to TEXT)
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

//class SQLiteDatabseHandler (ctx: Context) : ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1){
//
//    companion object {
//        private var instance: SQLiteDatabseHandler? = null
//
//        @Synchronized
//        fun getInstance(ctx: Context) : SQLiteDatabseHandler {
//            if (instance == null){
//                instance =
//                    SQLiteDatabseHandler(ctx.applicationContext)
//            }
//            return instance as SQLiteDatabseHandler
//        }
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        db?.createTable("EVENT_TABLE", true,
//            "ID" to INTEGER + PRIMARY_KEY + UNIQUE,
////            "EVENT_ID" to TEXT + UNIQUE,
//            "DATE" to TEXT,
//            "MONTH" to TEXT,
//            "YEAR" to TEXT,
//            "EVENT" to TEXT)
////            "EVENT_DATE" to TEXT)
//
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.dropTable("EVENT_TABLE", true)
//    }
//
//}
//
//val Context.db : SQLiteDatabseHandler
//    get() = SQLiteDatabseHandler.getInstance(
//        applicationContext
//    )