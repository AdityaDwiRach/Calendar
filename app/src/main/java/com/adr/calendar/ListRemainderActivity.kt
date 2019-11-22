package com.adr.calendar

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.coroutines.launch


class ListRemainderActivity : BaseActivity(){

    private var recycleVAdapter: RecycleVAdapter? = null
    //private var eventTable: EventTable? =
//    private var adapterRV
    var selectedEventID = 0

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
            recycleVAdapter?.notifyDataSetChanged()
        }

//        recycleVAdapter?.notifyDataSetChanged()

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
            IntentFilter("eventID-to-delete"))
    }

    private var mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context?,
            intent: Intent
        ) {
            selectedEventID = intent.getIntExtra("selectedDataID", 0)
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
//                Toast.makeText(this@ListRemainderActivity, "$selectedEventID", Toast.LENGTH_SHORT).show()
                startActivity(intent)
                finish()
                overridePendingTransition(0, 0)
//                overridePendingTransition(0, 2)
            }
            setNegativeButton("No"){_, _ ->
            }
        }.create().show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
