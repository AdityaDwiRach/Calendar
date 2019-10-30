package com.adr.calendar

import android.content.Context
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



class RecycleVAdapter(val context: Context, val items: ArrayList<EventTable>) : RecyclerView.Adapter<RecycleVAdapter.ViewHolder>(){

    var itemPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.event_list,
        parent, false))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
        itemPosition = position
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItems(items: EventTable){
            itemView.eventDate.text = items.date
            itemView.eventMonth.text = items.month
            itemView.eventYear.text = items.year
            itemView.eventName.text = items.event_name

            itemView.editButton.setOnClickListener{
                itemView.context.toast("Button edit clicked")
                itemView.context.startActivity<MainActivity>(
                    "oldDate" to items.date,
                    "oldMonth" to items.month,
                    "oldYear" to items.year,
                    "oldDEventName" to items.event_name
                )
            }
            itemView.deleteButton.setOnClickListener{
                itemView.context.database.use {
                    delete(EventTable.EVENT_TABLE, "EVENT_NAME = {event_name}",
                        "event_name" to items.event_name.toString())
                }
                itemView.context.toast("Button delete clicked")
            }

        }

    }

}