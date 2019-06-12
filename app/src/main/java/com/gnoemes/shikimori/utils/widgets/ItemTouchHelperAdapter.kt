package com.gnoemes.shikimori.utils.widgets

import com.gnoemes.shikimori.entity.rates.domain.RateSwipeAction

interface ItemTouchHelperAdapter {

    fun canDragItem(position: Int): Boolean

    fun canSwipeItem(position: Int): Boolean

    fun onItemMove(oldPosition: Int, newPosition: Int): Boolean

    fun onSelectedItemMove(newPosition: Int)

    fun onItemSwipeAction(pos: Int, action: RateSwipeAction)
}