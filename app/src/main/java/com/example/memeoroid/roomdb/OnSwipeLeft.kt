package com.example.memeoroid.roomdb

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.memeoroid.R

abstract class OnSwipeLeft(context: Context): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

    val updateColor = ContextCompat.getColor(context, R.color.updateColor)
    val updateIcon = R.drawable.ic_baseline_build_circle_24

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
//        RecyclerViewSwipeDecorator.Builder()
//        RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//            .addSwipteLeftBackgroundColor(updateColor)
//            .addSwipeLeftActionIcon(updateIcon)
//            .create()
//            .decorate()

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}