package com.adr.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.database
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.update
import org.jetbrains.anko.startActivity
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*


class MainActivity : AppCompatActivity() {

//    lateinit var testing

    private var currentDate: Long = 0
    private var currentDayOfMonth = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentEventName = null

    //cari cara untuk locaization array
    private var arrayOfMonth = arrayOf("January", "February", "March", "April", "Mei", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var oldDate = intent.getStringExtra("oldDate")
        var oldMonth = intent.getStringExtra("oldDMonth")
        var oldYear = intent.getStringExtra("oldDYear")
        var oldEventName = intent.getStringExtra("oldEventName")

        if (oldEventName.isNullOrBlank()){
            buttonUpdateEventList.isEnabled = false
        } else {
            buttonSave.isEnabled = false
            //set date, month, year in calendar widget
            editTextEventName.setText(oldEventName)
        }

        currentDate = calendarView.date

        calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            currentDayOfMonth = dayOfMonth
            currentMonth = month
            currentYear = year
        }

        buttonSave.setOnClickListener{
            addData()
            clearData()
        }

        buttonEventList.setOnClickListener{
            startActivity<ListRemainderActivity>()
        }

        buttonUpdateEventList.setOnClickListener{
            database.use {
                update(EventTable.EVENT_TABLE,
                    EventTable.EVENT_NAME to editTextEventName.text.toString()
                    //tambah date, month, year
                )
                    .whereArgs("${EventTable.EVENT_NAME} = {eventName}",
                    "eventName" to oldEventName)
                    .exec()
            }
            clearData()
        }
    }

    private fun addData() {
        database.use {
            insert(EventTable.EVENT_TABLE,
                EventTable.EVENT_NAME to editTextEventName.text.toString(),
                EventTable.DATE to currentDayOfMonth.toString(),
                EventTable.MONTH to arrayOfMonth[currentMonth],
                EventTable.YEAR to currentYear.toString()
            )
        }
    }

    private fun clearData(){
        editTextEventName.text.clear()
        calendarView.date = currentDate
    }
}
