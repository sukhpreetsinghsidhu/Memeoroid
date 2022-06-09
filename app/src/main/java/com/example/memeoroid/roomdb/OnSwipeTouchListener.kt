package com.example.memeoroid.roomdb

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class OnSwipeTouchListener: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }
}