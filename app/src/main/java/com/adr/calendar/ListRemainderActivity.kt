package com.adr.calendar

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.android.synthetic.main.activity_main.*
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
    private var calendarView: CalendarView? = null
    private var timePicker: TimePicker? = null
    private var eTNotes: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_remainder)

        arrayOfMonth = resources.getStringArray(R.array.months)

//        val oldDate = intent.getStringExtra("oldDate")
//        val oldMonth = intent.getStringExtra("oldMonth")
//        val oldYear = intent.getStringExtra("oldYear")
//        val oldEventName = intent.getStringExtra("oldEventName")
//        val oldHour = intent.getStringExtra("oldHour")
//        val oldMinute = intent.getStringExtra("oldMinute")
//        val oldID = intent.getIntExtra("oldID", 0)
        statusUpdate = intent.getBooleanExtra("statusUpdate", false)

        calendarView = findViewById(R.id.calendar_view)
        timePicker = findViewById(R.id.time_picker)

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
        val dialog = AlertDialog.Builder(this)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val calendar = layoutInflater.inflate(R.layout.alert_dialog_calendar, null)

        val sharedPreference =  getSharedPreferences("DATA",Context.MODE_PRIVATE)
        val state = sharedPreference.getBoolean("Shared Preference", false)

        calendarView = calendar.findViewById(R.id.calendar_view) as CalendarView

        if (state){
            val setDay = Integer.parseInt(sharedPreference.getString("date","").toString())
            val setMonth = arrayOfMonth?.indexOf(sharedPreference.getString("month","").toString())
            val setYear = Integer.parseInt(sharedPreference.getString("year","").toString())

            val calendarInstance = Calendar.getInstance()
            calendarInstance.set(Calendar.YEAR, setYear)
            calendarInstance.set(Calendar.MONTH, setMonth!!)
            calendarInstance.set(Calendar.DAY_OF_MONTH, setDay)

            val milliTime = calendarInstance.timeInMillis
            calendarView!!.setDate (milliTime, true, true)
        }

        calendarView?.setOnDateChangeListener{ _, year, month, dayOfMonth ->
            currentDayOfMonth = dayOfMonth
            currentMonth = month
            currentYear = year
        }
        dialog.setView(calendar)
        dialog.setTitle("Please choose date")
        dialog.setPositiveButton("Next") { _, _ ->
            alertDialogTime()
        }
        dialog.setNegativeButton(
            "Cancel"
        ) {_,_-> }
        dialog.create().show()
    }

//    fun alertDialogCalendar(date: String?, month: String?, year: String?, hour: String?, minute: String?, notes: String?, id: Int?){
//        val dialog = AlertDialog.Builder(applicationContext)
//        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val calendar = layoutInflater.inflate(R.layout.alert_dialog_calendar, null)
//        calendarView = calendar.findViewById(R.id.calendar_view) as CalendarView
//
//        val setDay = Integer.parseInt(date.toString())
//        val setMonth = arrayOfMonth?.indexOf(month.toString())
//        val setYear = Integer.parseInt(year.toString())
//
//        val calendarInstance = Calendar.getInstance()
//        calendarInstance.set(Calendar.YEAR, setYear)
//        calendarInstance.set(Calendar.MONTH, setMonth!!)
//        calendarInstance.set(Calendar.DAY_OF_MONTH, setDay)
//
//        val milliTime = calendarInstance.timeInMillis
//        calendarView!!.setDate (milliTime, true, true)
//
//        calendarView?.setOnDateChangeListener{ _, year, month, dayOfMonth ->
//            currentDayOfMonth = dayOfMonth
//            currentMonth = month
//            currentYear = year
//        }
//        dialog.setView(calendar)
//        dialog.setTitle("Please choose date")
//        dialog.setPositiveButton("Next") { _, _ ->
//            alertDialogTime()
//        }
//        dialog.setNegativeButton(
//            "Cancel"
//        ) {_,_-> }
//        dialog.create().show()
//
//        alertDialogTime(hour, minute, notes, id)
//    }

    private fun alertDialogTime(){
        val dialog = AlertDialog.Builder(this)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val calendar = layoutInflater.inflate(R.layout.alert_dialog_time, null)

        val sharedPreference =  getSharedPreferences("DATA",Context.MODE_PRIVATE)
        val state = sharedPreference.getBoolean("Shared Preference", false)

        timePicker = calendar.findViewById(R.id.time_picker) as TimePicker
        if (state){
            val hour = sharedPreference.getString("hour", "")
            val minute = sharedPreference.getString("minute", "")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                timePicker?.hour = hour!!.toInt()
                timePicker?.minute = minute!!.toInt()
            } else {
                timePicker?.currentHour = hour!!.toInt()
                timePicker?.currentMinute = minute!!.toInt()
            }
        }
        timePicker?.setOnTimeChangedListener { _, hourOfDay, minute ->
            currentHour = hourOfDay
            currentMinute = minute
        }
        dialog.setView(calendar)
        dialog.setTitle("Please choose time")
        dialog.setPositiveButton("Next") { _, _ ->
            alertDialogNotes()
        }
        dialog.setNegativeButton(
            "Cancel"
        ) {_,_-> }
        dialog.create().show()
    }

//    fun alertDialogTime(hour: String?, minute: String?, notes: String?, id: Int?){
//        val dialog = AlertDialog.Builder(applicationContext)
//        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val calendar = layoutInflater.inflate(R.layout.alert_dialog_time, null)
//        timePicker = calendar.findViewById(R.id.time_picker) as TimePicker
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            timePicker?.hour = hour!!.toInt()
//            timePicker?.minute = minute!!.toInt()
//        } else {
//            timePicker?.currentHour = hour!!.toInt()
//            timePicker?.currentMinute = minute!!.toInt()
//        }
//
//        timePicker?.setOnTimeChangedListener { _, hourOfDay, minute ->
//            currentHour = hourOfDay
//            currentMinute = minute
//        }
//
//        dialog.setView(calendar)
//        dialog.setTitle("Please choose time")
//        dialog.setPositiveButton("Next") { _, _ ->
//            alertDialogNotes()
//        }
//        dialog.setNegativeButton(
//            "Cancel"
//        ) {_,_-> }
//        dialog.create().show()
//
//        alertDialogNotes(notes, id)
//    }

    private fun alertDialogNotes(){
        val dialog = AlertDialog.Builder(this)
        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val calendar = layoutInflater.inflate(R.layout.alert_dialog_notes, null)

        val sharedPreference =  getSharedPreferences("DATA",Context.MODE_PRIVATE)
        val state = sharedPreference.getBoolean("Shared Preference", false)

        eTNotes = calendar.findViewById(R.id.et_notes) as EditText

        if (state){
            eTNotes!!.setText(sharedPreference.getString("notes", ""))
            val id = sharedPreference.getInt("id", 0)

            launch {
                val date = currentDayOfMonth.toString()
                val month = arrayOfMonth!![currentMonth]
                val year = currentYear.toString()
                val hour = currentHour.toString()
                val minute = currentMinute.toString()
                val requestCodeID = requestCodeIDGen + 1
                requestCodeIDGen = requestCodeID
                val eventName = currentEventName.toString()
                val mEventTable = EventTable(date, month.toString(), year, eventName, hour, minute,requestCodeID)
                mEventTable.id = id
                EventTableDatabase(applicationContext).getEventTableDao().updateData(mEventTable)
                Toast.makeText(applicationContext, "Data updated", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setView(calendar)
        dialog.setTitle("Please write note")
        dialog.setPositiveButton("Save") { _, _ ->
            currentEventName = eTNotes?.text.toString()
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
            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 23424243, intent, 0)
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent

            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            startActivity(Intent(applicationContext, ListRemainderActivity::class.java))
        }
        dialog.setNegativeButton(
            "Cancel"
        ) {_,_-> }
        dialog.create().show()
    }

//    fun alertDialogNotes(notes: String?, id: Int?){
//        val dialog = AlertDialog.Builder(applicationContext)
//        val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val calendar = layoutInflater.inflate(R.layout.alert_dialog_notes, null)
//        eTNotes = calendar.findViewById(R.id.et_notes) as EditText
//
//        eTNotes!!.setText(notes)
//
//        dialog.setView(calendar)
//        dialog.setTitle("Please write note")
//        dialog.setPositiveButton("Save") { _, _ ->
//            currentEventName = eTNotes?.text.toString()
//            launch {
//                val date = currentDayOfMonth.toString()
//                val month = arrayOfMonth!![currentMonth]
//                val year = currentYear.toString()
//                val hour = currentHour.toString()
//                val minute = currentMinute.toString()
//                val requestCodeID = requestCodeIDGen + 1
//                requestCodeIDGen = requestCodeID
//                val eventName = currentEventName.toString()
//                val mEventTable = EventTable(date, month.toString(), year, eventName, hour, minute,requestCodeID)
//                mEventTable.id = id!!
//                EventTableDatabase(applicationContext).getEventTableDao().updateData(mEventTable)
//                Toast.makeText(applicationContext, "Data updated", Toast.LENGTH_SHORT).show()
//            }
//
//            val calendar = Calendar.getInstance()
//            calendar.set(currentYear,currentMonth,currentDayOfMonth,currentHour,currentMinute, 0)
//
//            val intent = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
//            intent.putExtra("eventName", currentEventName.toString())
//            val pendingIntent = PendingIntent.getBroadcast(applicationContext, 23424243, intent, 0)
//            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
//            alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
//
//            finish()
//            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//            startActivity(Intent(applicationContext, ListRemainderActivity::class.java))
//        }
//        dialog.setNegativeButton(
//            "Cancel"
//        ) {_,_-> }
//        dialog.create().show()
//    }

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
