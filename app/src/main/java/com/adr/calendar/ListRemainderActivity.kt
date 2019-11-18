package com.adr.calendar

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.coroutines.launch


class ListRemainderActivity : BaseActivity(){

    private var recycleVAdapter: RecycleVAdapter? = null
    private var eventTable: EventTable? = null
    var selectedEventID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_remainder)

//        recycleVAdapter = RecycleVAdapter()

//        adapterRV = RecycleVAdapter(this, eventTable)
        recycleView.layoutManager = LinearLayoutManager(this)
        launch{
            val eventTable = EventTableDatabase(this@ListRemainderActivity).getEventTableDao().getAllData()
            recycleView.adapter = RecycleVAdapter(eventTable, this@ListRemainderActivity)
        }

        buttonDeleteTest.setOnClickListener {
            deleteData()
        }
    }

    private fun deleteData(){
//        val eventID = RecycleVAdapter(View)
        AlertDialog.Builder(this).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes"){_, _ ->
                launch {
                    EventTableDatabase(this@ListRemainderActivity).getEventTableDao().deleteData(eventTable!!)
                }
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
