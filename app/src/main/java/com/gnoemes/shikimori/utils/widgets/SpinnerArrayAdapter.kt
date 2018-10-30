package com.gnoemes.shikimori.utils.widgets

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ThemedSpinnerAdapter
import com.gnoemes.shikimori.utils.color


class SpinnerArrayAdapter(
        private val mainColor: Int,
        private val darkColor: Int,
        context: Context,
        private val itemRes: Int,
        private val items: List<String>
) : ArrayAdapter<Any>(context, itemRes, items), ThemedSpinnerAdapter {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.setTextColor(context.color(darkColor))
        view.background = ColorDrawable(context.color(mainColor))
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = super.getDropDownView(position, convertView, parent) as TextView
        v.setTextColor(context.color(darkColor))
        v.background = ColorDrawable(context.color(mainColor))
        return v
    }
}