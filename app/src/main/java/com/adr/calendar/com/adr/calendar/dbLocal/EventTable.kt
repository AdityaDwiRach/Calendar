package com.adr.calendar.com.adr.calendar.dbLocal

data class EventTable (var id: Long?, var date: String?, var month: String?, var year: String?, var event_name: String?, var hour: String?, var minute: String?){
    companion object {
        const val EVENT_TABLE:String = "EVENT_TABLE"
        const val ID:String = "ID"
        const val DATE:String = "DATE"
        const val MONTH:String = "MONTH"
        const val YEAR:String = "YEAR"
        const val EVENT_NAME:String = "EVENT_NAME"
        const val HOUR:String = "HOUR"
        const val MINUTE:String = "MINUTE"
    }
}