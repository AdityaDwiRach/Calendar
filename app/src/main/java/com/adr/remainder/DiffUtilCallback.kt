package com.adr.remainder

import androidx.recyclerview.widget.DiffUtil
import com.adr.remainder.dbLocal.EventTable

open class DiffUtilCallback(
    private val oldEventTable: List<EventTable>,
    private val newEventTable: List<EventTable>
):DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldEventTable.size
    }

    override fun getNewListSize(): Int {
        return newEventTable.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldEventTable[oldItemPosition] == newEventTable[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldEventTable[oldItemPosition] == newEventTable[newItemPosition]
    }
}