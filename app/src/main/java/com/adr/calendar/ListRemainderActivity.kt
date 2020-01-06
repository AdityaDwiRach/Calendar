package com.adr.calendar

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adr.calendar.com.adr.calendar.dbLocal.EventTable
import com.adr.calendar.com.adr.calendar.dbLocal.EventTableDatabase
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener
import kotlinx.android.synthetic.main.activity_list_remainder.*
import kotlinx.coroutines.launch


class ListRemainderActivity() : BaseActivity(), OnRapidFloatingActionContentLabelListListener<T>,
    Parcelable {

    private var recycleVAdapter: RecycleVAdapter? = null
//    private var eventTable: EventTable? = null
//    private var adapterRV
    var selectedEventID = 0
    private var notificationId = 0

    constructor(parcel: Parcel) : this() {
        selectedEventID = parcel.readInt()
        notificationId = parcel.readInt()
    }

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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(selectedEventID)
        parcel.writeInt(notificationId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListRemainderActivity> {
        override fun createFromParcel(parcel: Parcel): ListRemainderActivity {
            return ListRemainderActivity(parcel)
        }

        override fun newArray(size: Int): Array<ListRemainderActivity?> {
            return arrayOfNulls(size)
        }
    }
}
