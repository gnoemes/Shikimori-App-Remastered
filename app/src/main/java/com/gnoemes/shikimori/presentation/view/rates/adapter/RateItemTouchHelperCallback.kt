package com.gnoemes.shikimori.presentation.view.rates.adapter

import android.graphics.Canvas
import android.graphics.drawable.PaintDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.rates.domain.RateSwipeAction
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.widgets.ItemTouchHelperAdapter

class RateItemTouchHelperCallback(
        private val adapter: ItemTouchHelperAdapter,
        private val settingsSource: SettingsSource
) : ItemTouchHelper.Callback() {

    private val swipeToLeftAction by lazy { settingsSource.rateSwipeToLeftAction }
    private val swipeToRightAction by lazy { settingsSource.rateSwipeToRightAction }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = if (adapter.canDragItem(viewHolder.adapterPosition)) ItemTouchHelper.UP or ItemTouchHelper.DOWN else 0
        val swipeFlags = getSwipeFlags(viewHolder)
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    private fun getSwipeFlags(viewHolder: RecyclerView.ViewHolder): Int {
        return if (adapter.canSwipeItem(viewHolder.adapterPosition)) {
            var flags = 0
            if (swipeToLeftAction != RateSwipeAction.DISABLED) flags = flags or ItemTouchHelper.START
            if (swipeToRightAction != RateSwipeAction.DISABLED) flags = flags or ItemTouchHelper.END
            return flags
        } else 0
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val action = if (direction == ItemTouchHelper.START) swipeToLeftAction else swipeToRightAction
        adapter.onItemSwipeAction(viewHolder.adapterPosition, action)
    }

    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        adapter.onSelectedItemMove(toPos)
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (dX > 0) {
            val backgroundColor = when (swipeToRightAction) {
                RateSwipeAction.INCREMENT, RateSwipeAction.CHANGE -> recyclerView.context.colorAttr(R.attr.colorSecondaryTransparent)
                RateSwipeAction.ON_HOLD -> recyclerView.context.colorAttr(R.attr.colorRateOnHold)
                RateSwipeAction.DROP -> recyclerView.context.colorAttr(R.attr.colorRateDropped)
                else -> 0
            }

            val background = PaintDrawable(backgroundColor)
            val corners = recyclerView.context.dp(8).toFloat()
            background.setCornerRadius(corners)
            background.setBounds(viewHolder.itemView.left, viewHolder.itemView.top, viewHolder.itemView.left + dX.toInt() + corners.toInt(), viewHolder.itemView.bottom)
            background.draw(c)

            val iconColor = when (swipeToRightAction) {
                RateSwipeAction.INCREMENT, RateSwipeAction.CHANGE -> recyclerView.context.colorAttr(R.attr.colorSecondary)
                RateSwipeAction.ON_HOLD -> recyclerView.context.color(R.color.rate_on_hold_dark)
                RateSwipeAction.DROP -> recyclerView.context.color(R.color.rate_dropped_dark)
                else -> 0
            }

            val icon = when (swipeToRightAction) {
                RateSwipeAction.INCREMENT -> recyclerView.context.drawable(R.drawable.ic_plus_one_filled)
                RateSwipeAction.CHANGE -> recyclerView.context.drawable(R.drawable.ic_list_change)
                RateSwipeAction.ON_HOLD -> recyclerView.context.drawable(R.drawable.ic_pause_rate)
                RateSwipeAction.DROP -> recyclerView.context.drawable(R.drawable.ic_close)
                else -> null
            }

            if (icon != null) {
                val size = icon.intrinsicHeight
                val halfSize = size / 2
                val margin = recyclerView.context.dp(24)
                val top = viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfSize
                icon.setBounds(viewHolder.itemView.left + margin, top, viewHolder.itemView.left + margin + icon.intrinsicWidth, top + icon.intrinsicHeight)
                icon.tint(iconColor)
                icon.draw(c)
            }
        } else {
            val backgroundColor = when (swipeToLeftAction) {
                RateSwipeAction.INCREMENT, RateSwipeAction.CHANGE -> recyclerView.context.colorAttr(R.attr.colorSecondaryTransparent)
                RateSwipeAction.ON_HOLD -> recyclerView.context.colorAttr(R.attr.colorRateOnHold)
                RateSwipeAction.DROP -> recyclerView.context.colorAttr(R.attr.colorRateDropped)
                else -> 0
            }

            val background = PaintDrawable(backgroundColor)
            val corners = recyclerView.context.dp(8).toFloat()
            background.setCornerRadius(corners)
            background.setBounds(viewHolder.itemView.right + dX.toInt() - if (isCurrentlyActive) corners.toInt() else 0, viewHolder.itemView.top, viewHolder.itemView.right, viewHolder.itemView.bottom)
            background.draw(c)

            val iconColor = when (swipeToLeftAction) {
                RateSwipeAction.INCREMENT, RateSwipeAction.CHANGE -> recyclerView.context.colorAttr(R.attr.colorSecondary)
                RateSwipeAction.ON_HOLD -> recyclerView.context.color(R.color.rate_on_hold_dark)
                RateSwipeAction.DROP -> recyclerView.context.color(R.color.rate_dropped_dark)
                else -> 0
            }

            val icon = when (swipeToLeftAction) {
                RateSwipeAction.INCREMENT -> recyclerView.context.drawable(R.drawable.ic_plus_one_filled)
                RateSwipeAction.CHANGE -> recyclerView.context.drawable(R.drawable.ic_list_change)
                RateSwipeAction.ON_HOLD -> recyclerView.context.drawable(R.drawable.ic_pause_rate)
                RateSwipeAction.DROP -> recyclerView.context.drawable(R.drawable.ic_close)
                else -> null
            }

            if (icon != null) {
                val size = icon.intrinsicHeight
                val halfSize = size / 2
                val margin = recyclerView.context.dp(24)
                val top = viewHolder.itemView.top + (viewHolder.itemView.bottom - viewHolder.itemView.top) / 2 - halfSize
                icon.setBounds(viewHolder.itemView.right - margin - icon.intrinsicWidth, top, viewHolder.itemView.right - margin, top + icon.intrinsicHeight)
                icon.tint(iconColor)
                icon.draw(c)
            }
        }
    }
}