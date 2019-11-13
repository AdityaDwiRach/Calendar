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
import com.adr.calendar.com.adr.calendar.dbLocal.database
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.select_time.view.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.update
import org.jetbrains.anko.startActivity
import java.util.*
import android.content.DialogInterface
import android.widget.CheckBox
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import kotlinx.android.synthetic.main.select_time.*
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    private var currentDate: Long = 0
    private var currentDayOfMonth = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentEventName = null
    private var setOldDate: String? = null
    private var setOldMonth: String? = null
    private var setOldYear: String? = null
    private var setOldHour: String? = null
    private var setOldMinute: String? = null
    private var currentHour = 0
    private var currentMinute = 0

    //cari cara untuk locaization array
    private var arrayOfMonth = arrayOf("January", "February", "March", "April", "Mei", "June", "July", "August", "September", "October", "November", "December")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val oldDate = intent.getStringExtra("oldDate")
        val oldMonth = intent.getStringExtra("oldMonth")
        val oldYear = intent.getStringExtra("oldYear")
        val oldEventName = intent.getStringExtra("oldEventName")
        val oldHour = intent.getStringExtra("oldHour")
        val oldMinute = intent.getStringExtra("oldMinute")

        if (oldEventName == null){
            buttonUpdateEventList.isEnabled = false
        } else {
            buttonSave.isEnabled = false
            editTextEventName.setText(oldEventName)
            setOldDate = oldDate
            setOldMonth = oldMonth
            setOldYear = oldYear
            setOldHour = oldHour
            setOldMinute = oldMinute
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
            toast(currentHour.toString() + currentMinute.toString())
            clearData()
        }

        buttonDeleteAll.setOnClickListener {
            database.use {
                delete("EVENT_TABLE",null,null)
            }
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
                    EventTable.YEAR to currentYear,
                    EventTable.HOUR to currentHour,
                    EventTable.MINUTE to currentMinute
                )
                    .whereArgs("${EventTable.EVENT_NAME} = {eventName}",
                    "eventName" to oldEventName)
                    .exec()
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
                toast("Selected time : $currentHour : $currentMinute")
            }

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
                EventTable.YEAR to currentYear.toString(),
                EventTable.HOUR to currentHour.toString(),
                EventTable.MINUTE to currentMinute.toString()
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker1.hour = setOldHour!!.toInt()
            timePicker1.minute = setOldMinute!!.toInt()
        } else {
            timePicker1.currentHour = setOldHour!!.toInt()
            timePicker1.currentMinute = setOldMinute!!.toInt()
        }
    }
}
