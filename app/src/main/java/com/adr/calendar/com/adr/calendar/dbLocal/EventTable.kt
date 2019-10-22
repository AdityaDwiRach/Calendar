package com.adr.calendar.com.adr.calendar.dbLocal

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parceler
import kotlinx.android.parcel.Parcelize

data class EventTable (var id: Long?, var date: String?, var month: String?, var year: String?, var event_name: String?){
    companion object {
        const val EVENT_TABLE:String = "EVENT_TABLE"
        const val ID:String = "ID"
        const val DATE:String = "DATE"
        const val MONTH:String = "MONTH"
        const val YEAR:String = "YEAR"
        const val EVENT_NAME:String = "EVENT_NAME"
    }
}
//
//@Parcelize
//data class EventTable (val id: Long?, val date: String, val month: String, val year: String?, val event: String?):
//    Parcelable {
//
//    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun describeContents(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    companion object {
//        const val EVENT_TABLE : String = "EVENT_TABLE"
//        const val ID: String = "ID"
//        const val DATE: String = "EVENT_ID"
//        const val MONTH: String = "HOME_TEAM_NAME"
//        const val YEAR: String = "AWAY_TEAM_NAME"
//        const val EVENT: String = "HOME_SCORE"
//    }
//
//}