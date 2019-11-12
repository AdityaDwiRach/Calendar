package com.adr.calendar

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.database
import kotlinx.android.synthetic.main.event_list.view.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.update
import org.jetbrains.anko.notificationManager
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.nio.file.Files.size
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.view.*


class RecycleVAdapter(val context: Context, val items: ArrayList<EventTable>) : RecyclerView.Adapter<RecycleVAdapter.ViewHolder>(){

    var itemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_list,
        parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
        itemPosition = position

        holder.itemView.deleteButton.setOnClickListener{
            items.removeAt(position)
            notifyItemRemoved(position)
            holder.delete()
        }

        holder.itemView.editButton.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("oldDate", holder.eventDate)
            intent.putExtra("oldMonth", holder.eventMonth)
            intent.putExtra("oldYear", holder.eventYear)
            intent.putExtra("oldEventName", holder.eventName)
            intent.putExtra("oldHour", holder.eventHour)
            intent.putExtra("oldMinute", holder.eventMinute)
            startActivity(context, intent, null)
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var eventDate: String? = null
        var eventMonth: String? = null
        var eventYear: String? = null
        var eventName: String? = null
        var eventHour: String? = null
        var eventMinute: String? = null

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
        }

        fun delete(){
            itemView.context.database.use {
                delete(EventTable.EVENT_TABLE, "EVENT_NAME = {event_name}",
                    "event_name" to eventName.toString())
            }
        }

    }

}