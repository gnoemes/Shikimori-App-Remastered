package com.gnoemes.shikimori.presentation.view.search.filter.seasons.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ItemEntryInputBinding
import com.gnoemes.shikimori.entity.search.presentation.FilterEntryInput
import com.gnoemes.shikimori.utils.inflate
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate

class FilterSeasonInputAdapterDelegate(
        private val callback: (String) -> Unit
) : AbsListItemAdapterDelegate<FilterEntryInput, Any, FilterSeasonInputAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is FilterEntryInput

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemEntryInputBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: FilterEntryInput, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemEntryInputBinding) : RecyclerView.ViewHolder(binding.root) {

        private val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.i("DEVE", s.toString())
                val text = s?.toString()
                if (text != null && text.contains(" ")) wrapText(text)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.input.lineCount > 1) {
                    val text = s?.toString()?.let { it.substring(0, it.length - 1) }
                    if (text != null) wrapText(text)
                }
            }
        }

        init {
            binding.input.addTextChangedListener(listener)
        }

        fun bind(item: FilterEntryInput) {
            binding.input.setHint(R.string.filter_custom_input_hint)
        }

        private fun wrapText(value: String?) {
            if (!value.isNullOrBlank()) {
                callback.invoke(value.replace(Regex(" "), ""))
                binding.input.text = null
            }
        }

    }

}