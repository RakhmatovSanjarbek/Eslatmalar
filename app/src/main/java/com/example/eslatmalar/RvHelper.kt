package com.example.eslatmalar

import android.content.Context
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class RvHelper(
    private val toDoAdapter: ToDoAdapter
): ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val acssesOnMove=ItemTouchHelper.DOWN or ItemTouchHelper.UP
        val acssesOnSwipe=ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(acssesOnMove,acssesOnSwipe)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition=viewHolder.adapterPosition
        val toPosition=target.adapterPosition
        toDoAdapter.notifyItemMoved(fromPosition,toPosition)
        toDoAdapter.swapOnMove(fromPosition,toPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        toDoAdapter.deleteItemFromList(viewHolder.adapterPosition,)
        toDoAdapter.notifyItemRemoved(viewHolder.adapterPosition)
    }
}