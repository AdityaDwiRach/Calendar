package com.adr.calendar

import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.select_time.view.*
import java.util.*
import android.content.DialogInterface
import android.content.Intent
import android.widget.CheckBox
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.SyncStateContract.Helpers.update
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.select_time.*
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {

    private var currentDate: Long = 0
    private var currentDayOfMonth = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentEventName: String? = null
    private var setOldDate: String? = null
    private var setOldMonth: String? = null
    private var setOldYear: String? = null
    private var setOldHour = 0
    private var setOldMinute = 0
    private var currentHour = 0
    private var currentMinute = 0
    private var statusUpdate = false

    //cari cara untuk locaization array
    private var arrayOfMonth = arrayOf("January", "February", "March", "April", "Mei", "June", "July", "August", "September", "October", "November", "December")

    private var eventTable: EventTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val oldDate = intent.getStringExtra("oldDate")
        val oldMonth = intent.getStringExtra("oldMonth")
        val oldYear = intent.getStringExtra("oldYear")
        val oldEventName = intent.getStringExtra("oldEventName")
        val oldHour = intent.getStringExtra("oldHour")
        val oldMinute = intent.getStringExtra("oldMinute")
        val oldID = intent.getIntExtra("oldID", 0)
        statusUpdate = intent.getBooleanExtra("statusUpdate", false)

        if (!statusUpdate){
            buttonUpdateEventList.isEnabled = false
        } else {
            buttonSave.isEnabled = false
            editTextEventName.setText(oldEventName)
            setOldDate = oldDate
            setOldMonth = oldMonth
            setOldYear = oldYear
            setOldHour = oldHour!!.toInt()
            setOldMinute = oldMinute!!.toInt()
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
            currentEventName = editTextEventName.text.toString()
//            addData()
            launch {
                val date = currentDayOfMonth.toString()
                val month = arrayOfMonth[currentMonth]
                val year = currentYear.toString()
                val hour = currentHour.toString()
                val minute = currentMinute.toString()
                val timeFull = "$date $month $year $hour : $minute"
                val eventName = currentEventName.toString()
                val mEventTable = EventTable(date, month, year, eventName, hour, minute, timeFull)
                EventTableDatabase(this@MainActivity).getEventTableDao().addData(mEventTable)
                Toast.makeText(this@MainActivity, "Data saved", Toast.LENGTH_SHORT).show()
            }
            Toast.makeText(this, currentHour.toString() + currentMinute.toString(), Toast.LENGTH_SHORT).show()
            clearData()
        }
        
        buttonEventList.setOnClickListener{
            startActivity(Intent(this, ListRemainderActivity::class.java))
            finish()
        }

        buttonUpdateEventList.setOnClickListener{
            currentEventName = editTextEventName.text.toString()
            launch {
                val date = currentDayOfMonth.toString()
                val month = arrayOfMonth[currentMonth]
                val year = currentYear.toString()
                val hour = currentHour.toString()
                val minute = currentMinute.toString()
                val timeFull = "$date $month $year $hour : $minute"
                val eventName = currentEventName.toString()
                val mEventTable = EventTable(date, month, year, eventName, hour, minute, timeFull)
                mEventTable.id = oldID
                EventTableDatabase(it.context).getEventTableDao().updateData(mEventTable)
                Toast.makeText(it.context, "Data updated", Toast.LENGTH_SHORT).show()
            }
            clearData()
        }

        buttonSelectTime.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val pickers = layoutInflater.inflate(R.layout.select_time, null)
            val time = pickers.findViewById(R.id.timePicker1) as TimePicker
            time.is24HourView
            dialog.setView(pickers)
            dialog.setPositiveButton("Ok") { dialog, which ->
                time.clearFocus()
//                Notification.getTime(hour, minute, repeat_daily.isChecked)
//                Notification.scheduleNotification(this@AgendaActivity, 1)
            }
            dialog.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }

            val alertDialog = dialog.create()
            alertDialog.show()

            time.setOnTimeChangedListener { view, hourOfDay, minute ->
                currentHour = hourOfDay
                currentMinute = minute
            }

            pickers.buttonCheck.setOnClickListener {
                Toast.makeText(this, "Selected time : $currentHour : $currentMinute", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun clearData(){
        editTextEventName.text.clear()
        calendarView.date = currentDate
        statusUpdate = false
        if (!statusUpdate){
            buttonUpdateEventList.isEnabled = false
            buttonSave.isEnabled = true
        } else {
            buttonSave.isEnabled = false
            buttonUpdateEventList.isEnabled = true
        }
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

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            timePicker1.hour = setOldHour!!.toInt()
//            timePicker1.minute = setOldMinute!!.toInt()
//        } else {
//            timePicker1.currentHour = setOldHour!!.toInt()
//            timePicker1.currentMinute = setOldMinute!!.toInt()
//        }
    }
}
