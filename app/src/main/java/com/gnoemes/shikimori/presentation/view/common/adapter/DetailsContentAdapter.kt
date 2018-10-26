package com.gnoemes.shikimori.presentation.view.common.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeType
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideo
import com.gnoemes.shikimori.entity.anime.domain.AnimeVideoType
import com.gnoemes.shikimori.entity.common.domain.Related
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.DetailsAction
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaType
import com.gnoemes.shikimori.entity.roles.domain.Character
import com.gnoemes.shikimori.entity.roles.domain.Person
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.images.ImageLoader
import kotlinx.android.synthetic.main.item_content.view.*

class DetailsContentAdapter(private val imageLoader: ImageLoader,
                            private val settings: SettingsSource,
                            private val navigationCallback: (Type, Long) -> Unit,
                            private val detailsCallback: ((DetailsAction) -> Unit)?
) : RecyclerView.Adapter<DetailsContentAdapter.ViewHolder>() {

    private val items = mutableListOf<Any>()

    override fun getItemCount(): Int = items.size

    fun bindItems(newItems: List<Any>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(parent.inflate(R.layout.item_content))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        when (item) {
            is Anime -> holder.bindAnime(item)
            is Manga -> holder.bindManga(item)
            is Character -> holder.bindCharacter(item)
            is Person -> holder.bindPerson(item)
            is Related -> holder.bindRelated(item)
            is AnimeVideo -> holder.bindVideo(item)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindAnime(item: Anime) {
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)

                typeView.text = item.type.type
                typeView.visible()

                nameView.text = if (settings.isRomadziNaming) item.name else item.nameRu
                        ?: item.name

                desctiptionView.text = null
                desctiptionView.gone()
                cardView.onClick { navigationCallback.invoke(item.linkedType, item.id) }
            }
        }

        fun bindManga(item: Manga) {
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)

                typeView.text = item.type.type
                typeView.visible()

                nameView.text = if (settings.isRomadziNaming) item.name else item.nameRu
                        ?: item.name

                desctiptionView.text = null
                desctiptionView.gone()
                cardView.onClick { navigationCallback.invoke(item.linkedType, item.id) }
            }
        }

        fun bindCharacter(item: Character) {
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)

                typeView.gone()

                nameView.text = if (settings.isRomadziNaming) item.name else item.nameRu
                        ?: item.name

                desctiptionView.text = null
                desctiptionView.gone()
                cardView.onClick { navigationCallback.invoke(item.linkedType, item.id) }
            }
        }

        fun bindPerson(item: Person) {
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.image.original)

                typeView.gone()

                nameView.text = if (settings.isRomadziNaming) item.name else item.nameRu
                        ?: item.name

                desctiptionView.text = null
                desctiptionView.gone()
                cardView.onClick { navigationCallback.invoke(item.linkedType, item.id) }
            }
        }

        fun bindVideo(item: AnimeVideo) {
            with(itemView) {
                imageLoader.setImageListItem(imageView, item.imageUrl)

                typeView.text = item.hosting
                typeView.visible()

                nameView.text = if (!item.name.isNullOrBlank()) item.name else itemView.context.getString(getTitleFromType(item.type))

                desctiptionView.text = null
                desctiptionView.gone()

                cardView.onClick { detailsCallback?.invoke(DetailsAction.Video(item.url)) }
            }
        }

        //Todo delegate or more readable
        fun bindRelated(item: Related) {
            with(itemView) {
                val isAnime = item.type == Type.ANIME
                imageLoader.setImageListItem(imageView, if (isAnime) item.anime?.image?.original else item.manga?.image?.original)

                typeView.text = if (isAnime) item.anime?.type?.type else item.manga?.type?.type
                typeView.visible()

                nameView.text = when (settings.isRomadziNaming) {
                    true -> if (isAnime) item.anime?.name else item.manga?.name
                    else -> if (isAnime) item.anime?.nameRu
                            ?: item.anime?.name else item.manga?.nameRu ?: item.manga?.name
                }

                val descriptionText = "${item.relationRu}\n(${
                if (isAnime) getLocalizedType(item.anime?.type)
                else getLocalizedType(item.manga?.type)}, ${(
                        if (isAnime) item.anime?.dateAired
                        else item.manga?.dateAired
                        )?.year?.unknownIfZero()
                } г.)"
                desctiptionView.text = descriptionText
                desctiptionView.visible()
                cardView.onClick { navigationCallback.invoke(item.type, if (isAnime) item.anime?.id!! else item.manga?.id!!) }
            }
        }

        private fun getLocalizedType(type: AnimeType?): String? {
            return when (type) {
                AnimeType.TV -> itemView.context.getString(R.string.type_tv_translatable)
                AnimeType.OVA -> itemView.context.getString(R.string.type_ova)
                AnimeType.ONA -> itemView.context.getString(R.string.type_ona)
                AnimeType.MUSIC -> itemView.context.getString(R.string.type_music_translatable)
                AnimeType.MOVIE -> itemView.context.getString(R.string.type_movie_translatable)
                AnimeType.SPECIAL -> itemView.context.getString(R.string.type_special_translatable)
                else -> null
            }
        }

        private fun getLocalizedType(type: MangaType?): String? {
            return when (type) {
                MangaType.MANGA -> itemView.context.getString(R.string.type_manga_translatable)
                MangaType.NOVEL -> itemView.context.getString(R.string.type_novel_translatable)
                MangaType.ONE_SHOT -> itemView.context.getString(R.string.type_one_shot_translatable)
                MangaType.DOUJIN -> itemView.context.getString(R.string.type_doujin_translatable)
                MangaType.MANHUA -> itemView.context.getString(R.string.type_manhua_translatable)
                MangaType.MANHWA -> itemView.context.getString(R.string.type_manhwa_translatable)
                else -> null
            }
        }

        private fun getTitleFromType(videoType: AnimeVideoType): Int {
            return when (videoType) {
                AnimeVideoType.PROMO -> R.string.promo
                AnimeVideoType.ENDING -> R.string.ending
                AnimeVideoType.OPENING -> R.string.opening
                AnimeVideoType.OTHER -> R.string.other
            }
        }


    }


}