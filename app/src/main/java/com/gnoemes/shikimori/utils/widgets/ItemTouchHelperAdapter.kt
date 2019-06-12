package com.gnoemes.shikimori.utils.widgets

interface ItemTouchHelperAdapter {

    fun canDragItem(position : Int) : Boolean

    fun onItemMove(oldPosition: Int, newPosition: Int): Boolean

    fun onSelectedItemMove(newPosition: Int)

    fun onItemDismiss(pos: Int, direction: Int)
}