package com.adr.calendar

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog_calendar.calendarView
import kotlinx.android.synthetic.main.alert_dialog_time.*
import kotlinx.coroutines.launch
import java.util.*


class ListRemainderActivity : BaseActivity(){

    private var recycleVAdapter: RecycleVAdapter? = null
//    private var eventTable: EventTable? = null
//    private var adapterRV
    var selectedEventID = 0
    private var notificationId = 0
    private var doubleBackToExitPressedOnce = false
//    private var currentDate: Long = 0
    private var currentDayOfMonth = 0
    private var currentMonth = 0
    private var currentYear = 0
    private var currentEventName: String? = null
//    private var setOldDate: String? = null
//    private var setOldMonth: String? = null
//    private var setOldYear: String? = null
//    private var setOldHour = 0
//    private var setOldMinute = 0
    private var currentHour = 0
    private var currentMinute = 0
    private var statusUpdate = false
    private var requestCodeIDGen = 0
//    var time: TimePicker? = null

    var listRequestCode : List<Int>? = null
    private var arrayOfMonth : Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_remainder)

        arrayOfMonth = resources.getStringArray(R.array.months)

        val oldDate = intent.getStringExtra("oldDate")
        val oldMonth = intent.getStringExtra("oldMonth")
        val oldYear = intent.getStringExtra("oldYear")
        val oldEventName = intent.getStringExtra("oldEventName")
        val oldHour = intent.getStringExtra("oldHour")
        val oldMinute = intent.getStringExtra("oldMinute")
        val oldID = intent.getIntExtra("oldID", 0)
        statusUpdate = intent.getBooleanExtra("statusUpdate", false)

        recycleView.layoutManager = LinearLayoutManager(this)

        launch{
            val eventTable = EventTableDatabase(this@ListRemainderActivity).getEventTableDao().getAllData()

            recycleVAdapter = RecycleVAdapter(eventTable, this@ListRemainderActivity) { event ->
                deleteData(event)
            }

            recycleView.adapter = recycleVAdapter
        }

        val notificationManager = NotificationManagerCompat.from(this)

        if (intent != null && intent.getStringExtra("IntentListRemainder") == "CloseNotification"){
            notificationId = intent.getIntExtra("IntentListRemainder", 0)
            notificationManager.cancel(notificationId)
        }

        fab_list_remainder.setOnClickListener{
            Log.i("Testiiiing", "fab menu clicked")
//            startActivity(Intent(this, MainActivity::class.java))
            alertDialogCalendar()
        }
    }



    private fun deleteData(eventTable: EventTable){
        AlertDialog.Builder(this).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes"){_, _ ->
                launch {
                    EventTableDatabase(this@ListRemainderActivity).getEventTableDao().deleteData(eventTable)
                }
                finish()
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                startActivity(intent)
            }
            setNegativeButton("No"){_, _ ->
            }
        }.create().show()
    }

    private fun alertDialogCalendar(){
        AlertDialog.Builder(this).apply {
            setView(layoutInflater.inflate(R.layout.alert_dialog_calendar, null))
            setTitle("Please choose date")
            setCancelable(true)
            setPositiveButton("Next"){_, _ ->
                calendarView.setOnDateChangeListener{ _, year, month, dayOfMonth ->
//                    val msg = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
//                    Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()
                    currentDayOfMonth = dayOfMonth
                    currentMonth = month
                    currentYear = year
                }
                alertDialogTime()
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
    }

    fun alertDialogCalendar(date: String?, month: String?, year: String?, hour: String?, minute: String?, notes: String?){
        alertDialogTime(hour, minute, notes)
    }

    private fun alertDialogTime(){
        AlertDialog.Builder(this).apply {
            setView(layoutInflater.inflate(R.layout.alert_dialog_time, null))
            setTitle("Please choose time")
            setCancelable(true)
            setPositiveButton("Next"){_, _ ->
                timePicker1.setOnTimeChangedListener { _, hourOfDay, minute ->
                    currentHour = hourOfDay
                    currentMinute = minute
                }
                alertDialogNotes()
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
    }

    fun alertDialogTime(hour: String?, minute: String?, notes: String?){
        alertDialogNotes(notes)
    }

    private fun alertDialogNotes(){
        AlertDialog.Builder(this).apply {
            setView(layoutInflater.inflate(R.layout.alert_dialog_notes, null))
            setTitle("Please write note")
            setCancelable(true)
            setPositiveButton("Save"){_, _ ->
                currentEventName = editTextEventName.text.toString()
                launch {
                    val date = currentDayOfMonth.toString()
                    val month = arrayOfMonth!![currentMonth]
                    val year = currentYear.toString()
                    val hour = currentHour.toString()
                    val minute = currentMinute.toString()
                    val requestCodeID = requestCodeIDGen + 1
                    requestCodeIDGen = requestCodeID
                    val eventName = currentEventName.toString()
                    val mEventTable = EventTable(date,
                        month, year, eventName, hour, minute,requestCodeID)
                    EventTableDatabase(applicationContext).getEventTableDao().addData(mEventTable)
                    Toast.makeText(applicationContext, "Data saved", Toast.LENGTH_SHORT).show()
                }

                val calendar = Calendar.getInstance()
                calendar.set(currentYear,currentMonth,currentDayOfMonth,currentHour,currentMinute, 0)

                val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
                intent.putExtra("eventName", currentEventName.toString())
                val pendingIntent =
                    PendingIntent.getBroadcast(applicationContext, 23424243, intent, 0)
                val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
    }

    fun alertDialogNotes(notes: String?){

    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish()
        } else {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        }

        this.doubleBackToExitPressedOnce = true

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
