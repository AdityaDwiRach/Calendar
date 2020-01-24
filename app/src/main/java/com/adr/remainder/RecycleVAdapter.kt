package com.adr.remainder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.adr.remainder.dbLocal.EventTable
import kotlinx.android.synthetic.main.event_list.view.*


class RecycleVAdapter(var items : List<EventTable>, val context: Context, val item: (event: EventTable) -> Unit) : RecyclerView.Adapter<RecycleVAdapter.ViewHolder>(){

    var itemPosition = 0

//    private var eventTable: EventTable? = null

    var selectedDataID = 0

    var selectedDataRequestCode: Int = 0

//    override val coroutineContext: CoroutineContext
//        get() = job + Dispatchers.Main

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_list,
            parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(items[position])

//        itemPosition = position

        holder.itemView.setOnClickListener {
            selectedDataRequestCode = holder.eventRequestCodeID
            Toast.makeText(context, selectedDataRequestCode.toString(), Toast.LENGTH_SHORT).show()


//            val intent = Intent("eventID-to-delete")
//            intent.putExtra("selectedDataID", selectedDataID)
//            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }

        holder.itemView.deleteButton.setOnClickListener{
            item.invoke(items[position])
//            notifyDataSetChanged()
        }

        holder.itemView.editButton.setOnClickListener {
            val date = holder.eventDate
            val month = holder.eventMonth
            val year = holder.eventYear
            val hour = holder.eventHour
            val minute = holder.eventMinute
            val notes = holder.eventName
            val id = holder.eventID

            val sharedPreference =  context.getSharedPreferences("DATA",Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putBoolean("Shared Preference", true)
            editor.putString("date",date)
            editor.putString("month",month)
            editor.putString("year",year)
            editor.putString("hour",hour)
            editor.putString("minute",minute)
            editor.putString("notes",notes)
            editor.putInt("id", id!!)
            editor.apply()
//            ListRemainderActivity().alertDialogCalendar(date, month, year, hour, minute, notes, id)
//            val intent = Intent(context, MainActivity::class.java)
//            intent.putExtra("oldDate", holder.eventDate)
//            intent.putExtra("oldMonth", holder.eventMonth)
//            intent.putExtra("oldYear", holder.eventYear)
//            intent.putExtra("oldEventName", holder.eventName)
//            intent.putExtra("oldHour", holder.eventHour)
//            intent.putExtra("oldMinute", holder.eventMinute)
//            intent.putExtra("oldID", holder.eventID)
//            intent.putExtra("statusUpdate", true)
//            startActivity(context, intent, null)
        }

    }
//
//    fun updateData(newEventTable: List<EventTable>){
//        this.items = newEventTable
//        notifyDataSetChanged()
//    }

//    fun updateData(newEventTable: List<EventTable>){
//        val diffCallback = DiffUtilCallback(items, newEventTable)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        diffResult.dispatchUpdatesTo(this)
//        items = newEventTable
//
////        this.items = newEventTable
//    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var eventDate : String? = null
        var eventMonth : String? = null
        var eventYear : String? = null
        var eventName : String? = null
        var eventHour : String? = null
        var eventMinute : String? = null
        var eventID : Int? = null
        var eventRequestCodeID : Int = 0

        fun bindItems(
            items: EventTable
        ){
            itemView.eventDate.text = items.date
            itemView.eventMonth.text = items.month
            itemView.eventYear.text = items.year
            itemView.eventName.text = items.event_name
            itemView.eventHour.text = "${items.hour} : "
            itemView.eventMinute.text = items.minute

            eventDate = items.date
            eventMonth = items.month
            eventYear = items.year
            eventName = items.event_name
            eventHour = items.hour
            eventMinute = items.minute
            eventID = items.id
            eventRequestCodeID = items.request_code_id
        }


    }
}