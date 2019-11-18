package com.adr.calendar

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import kotlinx.android.synthetic.main.event_list.view.*
import androidx.core.content.ContextCompat.startActivity
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.util.SparseBooleanArray




class RecycleVAdapter(val items : List<EventTable>, val context: Context) : RecyclerView.Adapter<RecycleVAdapter.ViewHolder>(), CoroutineScope{

    lateinit var job: Job

    var itemPosition = 0

//    private var eventTable: EventTable? = null

    var selectedDataID = 0

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_list,
            parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
        itemPosition = position

        holder.itemView.setOnClickListener {
            selectedDataID = holder.eventID!!
            Toast.makeText(context, holder.eventID.toString(), Toast.LENGTH_SHORT).show()
        }

        holder.itemView.deleteButton.setOnClickListener{
            holder.eventID
//            notifyItemRemoved(position)
//            db.delete()
//            deleteData()
        }

        holder.itemView.editButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("oldDate", holder.eventDate)
            intent.putExtra("oldMonth", holder.eventMonth)
            intent.putExtra("oldYear", holder.eventYear)
            intent.putExtra("oldEventName", holder.eventName)
            intent.putExtra("oldHour", holder.eventHour)
            intent.putExtra("oldMinute", holder.eventMinute)
            intent.putExtra("oldID", holder.eventID)
            intent.putExtra("statusUpdate", true)
            startActivity(context, intent, null)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var eventDate : String? = null
        var eventMonth : String? = null
        var eventYear : String? = null
        var eventName : String? = null
        var eventHour : String? = null
        var eventMinute : String? = null
        var eventID : Int? = null

        fun bindItems(items: EventTable){
            itemView.eventDate.text = items.date
            itemView.eventMonth.text = items.month
            itemView.eventYear.text = items.year
            itemView.eventName.text = items.event_name
            itemView.eventHour.text = items.hour
            itemView.eventMinute.text = items.minute

            eventDate = items.date
            eventMonth = items.month
            eventYear = items.year
            eventName = items.event_name
            eventHour = items.hour
            eventMinute = items.minute
            eventID = items.id
        }
    }
}