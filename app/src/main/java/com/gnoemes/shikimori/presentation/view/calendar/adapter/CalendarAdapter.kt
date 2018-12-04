package com.gnoemes.shikimori.presentation.view.calendar.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.calendar.presentation.CalendarViewModel
import com.gnoemes.shikimori.presentation.view.common.adapter.StartSnapHelper
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.widgets.HorizontalSpaceItemDecorator
import com.gnoemes.shikimori.utils.widgets.SparseIntArrayParcelable
import kotlinx.android.synthetic.main.item_calendar.view.*

class CalendarAdapter(
        private val imageLoader: ImageLoader,
        private val callback: (id: Long) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    private companion object {
        const val NESTED_STATES_KEY = "NESTED_STATES_KEY"
    }

    private val sharedPool = RecyclerView.RecycledViewPool()
    private var nestedStates = SparseIntArrayParcelable()
    private val items = mutableListOf<CalendarViewModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun bindItems(viewModels: List<CalendarViewModel>) {
        val oldData = items.toMutableList()
        items.clear()
        items.addAll(viewModels)

        DiffUtil.calculateDiff(DiffCallback(oldData, items))
                .dispatchUpdatesTo(this)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)

        holder.itemView.apply {
            recyclerView.removeOnScrollListener(holder.nestedScrollListener)
            recyclerView.adapter = null
            dateTextView.text = null
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].date.hashCode().toLong()
    }

    fun onSaveInstanceState(): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(NESTED_STATES_KEY, nestedStates)
        return bundle
    }

    fun onRestoreInstanceState(bundle: Bundle) {
        val restoredStates = bundle.getParcelable<SparseIntArrayParcelable>(NESTED_STATES_KEY)
        if (restoredStates != null) {
            nestedStates = restoredStates
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val snapOffset = itemView.resources.getDimension(R.dimen.margin_normal).toInt()
        private lateinit var item: CalendarViewModel
        private val adapter by lazy { CalendarAnimeAdapter(itemView.context, imageLoader, callback, item.items).apply { if (!hasObservers()) setHasStableIds(true) } }

        val nestedScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = itemView.recyclerView.layoutManager as? LinearLayoutManager
                if (layoutManager != null) {
                    nestedStates.put(itemId.toInt(), layoutManager.findFirstCompletelyVisibleItemPosition())
                }
            }
        }

        init {
            val snapHelper = StartSnapHelper(snapOffset)
            snapHelper.attachToRecyclerView(itemView.recyclerView)
            itemView.recyclerView.apply {
                setHasFixedSize(true)
                setRecycledViewPool(sharedPool)
                setItemViewCacheSize(20)
                addItemDecoration(HorizontalSpaceItemDecorator(resources.getDimension(R.dimen.margin_small).toInt(), snapOffset))
            }
        }

        fun bind(item: CalendarViewModel) {
            this.item = item
            with(itemView) {
                dateTextView.text = item.date

                with(recyclerView) {
                    adapter = this@ViewHolder.adapter
                    layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false).apply { initialPrefetchItemCount = 3 }
                    addOnScrollListener(nestedScrollListener)

                    val savedPosition = nestedStates[itemId.toInt()]
                    (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(savedPosition, snapOffset)
                }
            }
        }
    }

    private inner class DiffCallback(
            private val oldItems: MutableList<CalendarViewModel>,
            private val newItems: MutableList<CalendarViewModel>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldItems.size
        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }

}