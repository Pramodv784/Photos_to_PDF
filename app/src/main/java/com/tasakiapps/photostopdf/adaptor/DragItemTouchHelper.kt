package com.tasakiapps.photostopdf.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("ClickableViewAccessibility")
class DragItemTouchHelper(
    private val adapter: SelectedImageAdapter,
    private val context: Context,
    private val recyclerView: RecyclerView
    ) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {

        interface ItemTouchHelperAdapter {
            fun onItemMove(fromPosition: Int, toPosition: Int)
        }

        private var dragFrom = -1
        private var dragTo = -1

        private val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                val view = recyclerView.findChildViewUnder(e.x, e.y)
                view?.let {
                    val longPressedIndex = recyclerView.getChildAdapterPosition(view)
                    if (longPressedIndex != RecyclerView.NO_POSITION) {
                        dragFrom = longPressedIndex
                        val shadowBuilder = View.DragShadowBuilder(view)
                        view.startDrag(null, shadowBuilder, view, 0)
                    }
                }
                super.onLongPress(e)
            }
        })

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            if (dragFrom == -1) {
                dragFrom = fromPosition
            }
            dragTo = toPosition

            adapter.onItemMove(fromPosition, toPosition)
            return true
        }



        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder?.itemView?.setBackgroundColor(Color.LTGRAY)
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.setBackgroundColor(0)
            if (dragFrom != -1 && dragTo != -1 && dragFrom != dragTo) {
                adapter.onItemMove(dragFrom, dragTo)
            }
            dragFrom = -1
            dragTo = -1
        }



        override fun getDragDirs(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return ItemTouchHelper.UP or ItemTouchHelper.DOWN
        }

        override fun onSwiped(
            viewHolder: RecyclerView.ViewHolder,
            direction: Int
        ) {
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
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return false
        }

        override fun getBoundingBoxMargin(): Int {
            return 0
        }

        override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.5f
        }

        override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
            return 0.5f
        }

        init {
            val itemTouchHelper = ItemTouchHelper(this)
            itemTouchHelper.attachToRecyclerView(recyclerView)
            recyclerView.setOnTouchListener { v, event ->
                gestureDetector.onTouchEvent(event)
            }
        }
    }
