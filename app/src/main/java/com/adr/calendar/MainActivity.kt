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

        buttonSelectTime.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val pickers = layoutInflater.inflate(R.layout.select_time, null)
            val time = pickers.findViewById(R.id.timePicker1) as TimePicker
//            val repeat_daily = pickers.findViewById(R.id.repeat_daily) as CheckBox
            dialog.setView(pickers)
            dialog.setPositiveButton("Ok") { dialog, which ->
                time.clearFocus()
                val hour = time.currentHour
                val minute = time.currentMinute

                Toast.makeText(this, "Selected time : $hour : $minute", Toast.LENGTH_LONG).show()

//                Notification.getTime(hour, minute, repeat_daily.isChecked)
//                Notification.scheduleNotification(this@AgendaActivity, 1)
            }
            dialog.setNegativeButton(
                "Cancel"
            ) { dialog, which -> dialog.dismiss() }

            val alertDialog = dialog.create()
            alertDialog.show()

        }

//        val picker= findViewById<TimePicker>(R.id.timePicker1)
//        val textView = findViewById<TextView>(R.id.textView)
//        timePicker1.is24HourView

//        buttonSelectTime.setOnClickListener {
//            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val mDialogView = layoutInflater.inflate(R.layout.select_time, null)
//            //AlertDialogBuilder
//            val mBuilder = AlertDialog.Builder(this)
//                .setView(mDialogView)
//                .setTitle("Select Time")
//            //show dialog
//            val  mAlertDialog = mBuilder.show()
//
////            mDialogView.picker.setOnTimeChangedListener { _, hour, minute -> var hour = hour
////                var am_pm = ""
////                // AM_PM decider logic
////                when {hour == 0 -> { hour += 12
////                    am_pm = "AM"
////                }
////                    hour == 12 -> am_pm = "PM"
////                    hour > 12 -> { hour -= 12
////                        am_pm = "PM"
////                    }
////                    else -> am_pm = "AM"
////                }
////                if (textView != null) {
////                    val hour = if (hour < 10) "0" + hour else hour
////                    val min = if (minute < 10) "0" + minute else minute
////                    // display format of time
////                    val msg = "Time is: $hour : $min $am_pm"
////                    textView.text = msg
////                    textView.visibility = ViewGroup.VISIBLE
////                }
////            }
//            //login button click of custom layout
////            picker.onTimeChanged { view, hourOfDay, minute ->  }
//            mDialogView.buttonExample.setOnClickListener {
//                var hour: Int
//                val minute: Int
//                val am_pm: String
//                if (Build.VERSION.SDK_INT >= 23) {
//                    hour = picker.hour
//                    minute = picker.minute
//                } else {
//                    hour = picker.currentHour
//                    minute = picker.currentMinute
//                }
//                if (hour > 12) {
//                    am_pm = "PM"
//                    hour = hour - 12
//                } else {
//                    am_pm = "AM"
//                }
//                Toast.makeText(this, "Selected Date: $hour:$minute $am_pm", Toast.LENGTH_LONG).show()
////                tvw.setText("Selected Date: $hour:$minute $am_pm")
//                //dismiss dialog
////                mAlertDialog.dismiss()
//                //get text from EditTexts of custom layout
////                val name = mDialogView.dialogNameEt.text.toString()
////                val email = mDialogView.dialogEmailEt.text.toString()
////                val password = mDialogView.dialogPasswEt.text.toString()
//                //set the input text in TextView
////                mainInfoTv.setText("Name:"+ name +"\nEmail: "+ email +"\nPassword: "+ password)
//
//            }
////            cancel button click of custom layout
////            mDialogView.dialogCancelBtn.setOnClickListener {
////                //dismiss dialog
////                mAlertDialog.dismiss()
////            }
//        }dismiss
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
