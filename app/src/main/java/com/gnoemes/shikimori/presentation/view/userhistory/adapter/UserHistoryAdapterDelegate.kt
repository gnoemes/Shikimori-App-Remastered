package com.gnoemes.shikimori.presentation.view.userhistory.adapter

import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.gnoemes.shikimori.databinding.ItemUserHistoryBinding
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryViewModel
import com.gnoemes.shikimori.utils.Utils
import com.bumptech.glide.Glide
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.onClick
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate


class UserHistoryAdapterDelegate(
        private val imageLoader: ImageLoader,
        private val callback: (Type, Long) -> Unit
) : AbsListItemAdapterDelegate<UserHistoryViewModel, Any, UserHistoryAdapterDelegate.ViewHolder>() {

    override fun isForViewType(item: Any, items: MutableList<Any>, position: Int): Boolean =
            item is UserHistoryViewModel && item.target != null

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
            ViewHolder(ItemUserHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(item: UserHistoryViewModel, holder: ViewHolder, payloads: MutableList<Any>) {
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemUserHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var item: UserHistoryViewModel

        init {
            binding.container.onClick { callback.invoke(item.target?.linkedType!!, item.target?.linkedId!!) }
        }

        fun bind(item: UserHistoryViewModel) {
            this.item = item
            with(binding) {
                targetNameView.text = item.targetName
                actionView.text = Html.fromHtml(item.action)
                dateView.text = item.actionDateString

                Glide.with(root)
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