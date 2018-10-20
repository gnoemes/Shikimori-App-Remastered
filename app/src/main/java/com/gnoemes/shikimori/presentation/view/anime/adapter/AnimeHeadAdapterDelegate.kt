package com.gnoemes.shikimori.presentation.view.anime.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.aesthetic.utils.tint
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.presentation.AnimeHeadItem
import com.gnoemes.shikimori.entity.common.domain.SpinnerAction
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.presentation.view.common.adapter.GenreAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.google.android.flexbox.FlexboxLayoutManager
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_details_head.view.*

class AnimeHeadAdapterDelegate(private val imageLoader: ImageLoader,
                               private val detailsCallback: (DetailsAction) -> Unit,
                               private val settings: SettingsSource
) : AbsListItemAdapterDelegate<AnimeHeadItem, Any, AnimeHeadAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is AnimeHeadItem

    override fun onBindViewHolder(item: AnimeHeadItem, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_details_head))

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val genreAdapter by lazy { GenreAdapter(detailsCallback) }

        fun bind(item: AnimeHeadItem) {
            with(itemView) {
                imageLoader.setImageWithPlaceHolder(imageView, item.image.original)


                imageView.onClick { detailsCallback.invoke(DetailsAction.Screenshots) }
                studioView.onClick { detailsCallback.invoke(DetailsAction.StudioClicked(item.studio!!.id)) }
                studioView.visibleIf { item.studio != null }

                val nameText = if (settings.isRomadziNaming) item.name else item.nameRu ?: item.name
                val nameSecondText = if (settings.isRomadziNaming) item.nameRu
                        ?: item.name else item.name

                nameView.text = nameText
                nameSecondView.text = nameSecondText

                val typeText = context.getString(R.string.details_type).toBold().append(" ").append(item.type)
                val seasonText = context.getString(R.string.details_season).toBold().append(" ").append(item.season)
                val statusText = context.getString(R.string.details_status).toBold().append(" ").append(item.status)
                val studioText = context.getString(R.string.details_studio).toBold().append(" ").append(item.studio?.name
                        ?: "")

                typeView.text = typeText
                seasonView.text = seasonText
                statusView.text = statusText
                studioView.text = studioText

                ratingView.rating = (item.score / 2).toFloat()
                ratingValueView.text = item.score.toString()

                rateSpinnerView.setRateStatus(item.rateStatus)
                rateSpinnerView.callback = { spinnerAction, rateStatus ->
                    when (spinnerAction) {
                        SpinnerAction.RATE_CHANGE -> detailsCallback.invoke(DetailsAction.ChangeRateStatus(rateStatus))
                        SpinnerAction.RATE_EDIT -> detailsCallback.invoke(DetailsAction.EditRate)
                    }
                }

                val onlineDrawable = context.drawable(R.drawable.ic_play_circle_outline).also { it.tint(context.colorAttr(R.attr.colorOnAccent)) }
                watchOnlineBtn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(onlineDrawable, null, null, null)
                    setText(R.string.details_watch_online)
                    setOnClickListener { detailsCallback.invoke(DetailsAction.WatchOnline) }
                }

                with(genreList) {
                    adapter = genreAdapter
                    layoutManager = FlexboxLayoutManager(context)
                }

                genreAdapter.bindItems(item.genres)
            }

        }

        private fun String.toBold(): SpannableStringBuilder {
            val builder = SpannableStringBuilder()
                    .append(this)
            builder.setSpan(StyleSpan(Typeface.BOLD), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return builder
        }
    }

}
