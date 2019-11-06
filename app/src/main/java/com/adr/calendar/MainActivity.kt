package com.adr.calendar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.database
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.update
import org.jetbrains.anko.startActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    private var currentDate: Long = 0
    private var currentDayOfMonth = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentEventName = null
    private var setOldDate: String? = null
    private var setOldMonth: String? = null
    private var setOldYear: String? = null

    //cari cara untuk locaization array
    private var arrayOfMonth = arrayOf("January", "February", "March", "April", "Mei", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val oldDate = intent.getStringExtra("oldDate")
        val oldMonth = intent.getStringExtra("oldMonth")
        val oldYear = intent.getStringExtra("oldYear")
        var oldEventName = intent.getStringExtra("oldEventName")

        if (oldEventName == null){
            buttonUpdateEventList.isEnabled = false
        } else {
            buttonSave.isEnabled = false
            editTextEventName.setText(oldEventName)
            setOldDate = oldDate
            setOldMonth = oldMonth
            setOldYear = oldYear
            Log.i("Testiiiiiing", oldDate + oldMonth + oldYear + oldEventName)
            setDateAfterUpdate()
        }

        currentDate = calendarView.date

        Log.i("Testiiiing", currentDate.toString())

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
                    EventTable.EVENT_NAME to editTextEventName.text.toString(),
                    EventTable.DATE to currentDayOfMonth,
                    EventTable.MONTH to currentMonth,
                    EventTable.YEAR to currentYear
                )
                    .whereArgs("${EventTable.EVENT_NAME} = {eventName}",
                    "eventName" to oldEventName)
                    .exec()
            }
            clearData()
        }
    }

    override fun onResume() {
        super.onResume()
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

    private fun setDateAfterUpdate(){

        val day = Integer.parseInt(setOldDate.toString())
        val month = arrayOfMonth.indexOf(setOldMonth.toString())
        val year = Integer.parseInt(setOldYear.toString())

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        val milliTime = calendar.timeInMillis
        calendarView.setDate (milliTime, true, true)
    }
}
