package com.gnoemes.shikimori.presentation.view.search.filter.seasons.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryInput
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_entry_input.view.*

class FilterSeasonInputAdapterDelegate(
        private val callback: (String) -> Unit
) : AbsListItemAdapterDelegate<FilterEntryInput, Any, FilterSeasonInputAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterEntryInput

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_entry_input))

    override fun onBindViewHolder(item: FilterEntryInput, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i("DEVE", s?.toString())
                if (s?.toString()?.contains(" ") == true) wrapText(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (itemView.input.lineCount > 1) wrapText(s?.toString()?.let { it.substring(0, it.length - 1) })
            }
        }

        init {
            itemView.input.addTextChangedListener(listener)
        }

        fun bind(item: FilterEntryInput) {
            itemView.input.setHint(R.string.filter_custom_input_hint)
        }

        private fun wrapText(value: String?) {
            if (!value.isNullOrBlank()) {
                callback.invoke(value.replace(Regex(" "), ""))
                itemView.input.text = null
            }
        }

    }

}