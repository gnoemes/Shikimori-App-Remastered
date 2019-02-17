package com.gnoemes.shikimori.presentation.view.common.widget


import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

open class ReSpinner : AppCompatSpinner {

    var itemClickListener: OnItemClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    override fun setOnItemClickListener(l: OnItemClickListener?) {
        this.itemClickListener = l
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        performItemClick()
    }

    override fun setSelection(position: Int, animate: Boolean) {
        super.setSelection(position, animate)
        if (animate) {
            performItemClick()
        }
    }

    private fun performItemClick() {
        itemClickListener?.onItemClick(this, selectedView, selectedItemPosition, selectedItemId)
    }
}