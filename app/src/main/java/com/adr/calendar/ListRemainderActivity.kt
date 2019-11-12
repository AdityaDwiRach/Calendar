package com.adr.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.database
import kotlinx.android.synthetic.main.activity_list_remainder.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class ListRemainderActivity : AppCompatActivity() {

    private lateinit var adapterRV: RecycleVAdapter
    private var eventTable = ArrayList<EventTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_remainder)

        adapterRV = RecycleVAdapter(this, eventTable)
        recycleView.setAdapter(adapterRV)
        recycleView.layoutManager = LinearLayoutManager(this)
        getData()
        adapterRV.notifyDataSetChanged()
    }

    private fun getData() {
        database.use{
            eventTable.clear()
            val result = select(EventTable.EVENT_TABLE)
            val eventData = result.parseList(classParser<EventTable>())
            eventTable.addAll(eventData)
            adapterRV.notifyDataSetChanged()
        }
    }
}
