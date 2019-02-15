package com.gnoemes.shikimori.presentation.view.userhistory.adapter

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.gnoemes.shikimori.utils.Utils
import com.gnoemes.shikimori.utils.images.GlideApp
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.inflate
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.synthetic.main.item_user_history.view.*



class UserHistoryAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<UserHistoryViewModel, Any, UserHistoryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserHistoryViewModel && item.target != null

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(parent.inflate(com.gnoemes.shikimori.R.layout.item_user_history))

    override fun onBindViewHolder(item: UserHistoryViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private lateinit var item: UserHistoryViewModel

        init {
            itemView.container.onClick { callback.invoke(item.target?.linkedType!!, item.target?.linkedId!!) }
        }

        fun bind(item: UserHistoryViewModel) {
            this.item = item
            with(itemView) {
                targetNameView.text = item.target?.linkedName
                actionView.text = Html.fromHtml(item.action)
                dateView.text = item.actionDateString

                GlideApp.with(itemView)
                        .asBitmap()
                        .load(item.target?.imageUrl)
                        .into(object : BitmapImageViewTarget(targetImageView) {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                super.onResourceReady(resource, transition)

                                val defaultColor = Utils.getDominantColor(resource)

                                Palette.Builder(resource).generate {
                                    val dominantColor = it?.getLightVibrantColor(defaultColor)!!

                                    val gd = GradientDrawable(
                                            GradientDrawable.Orientation.LEFT_RIGHT,
                                            intArrayOf(dominantColor, dominantColor, ColorUtils.setAlphaComponent(dominantColor, 0)))
                                    gd.cornerRadius = 0f
                                    firstLayerGradient.setImageDrawable(gd)
                                }
                            }
                        })
            }
        }

    }
}