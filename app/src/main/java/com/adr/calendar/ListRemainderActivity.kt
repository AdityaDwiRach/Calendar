package com.adr.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.coroutines.launch


class ListRemainderActivity : BaseActivity(){

    private var recycleVAdapter: RecycleVAdapter? = null
//    private var eventTable: EventTable? = null
//    private var adapterRV
    var selectedEventID = 0
    private var notificationId = 0
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_remainder)

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
                alertDialogTime()
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
    }

    private fun alertDialogTime(){
        AlertDialog.Builder(this).apply {
            setView(layoutInflater.inflate(R.layout.alert_dialog_time, null))
            setTitle("Please choose time")
            setCancelable(true)
            setPositiveButton("Next"){_, _ ->
                alertDialogNotes()
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
    }

    private fun alertDialogNotes(){
        AlertDialog.Builder(this).apply {
            setView(layoutInflater.inflate(R.layout.alert_dialog_notes, null))
            setTitle("Please write note")
            setCancelable(true)
            setPositiveButton("Save"){_, _ ->
            }
            setNegativeButton("Cancel"){_, _ ->
            }
        }.create().show()
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
