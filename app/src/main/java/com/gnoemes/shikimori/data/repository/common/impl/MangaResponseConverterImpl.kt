package com.gnoemes.shikimori.data.repository.common.impl

import com.gnoemes.shikimori.data.repository.common.ImageResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.entity.manga.data.MangaResponse
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.utils.appendHostIfNeed
import javax.inject.Inject

class MangaResponseConverterImpl @Inject constructor(
        private val imageConverter: ImageResponseConverter
) : MangaResponseConverter {
    override fun apply(t: List<MangaResponse?>): List<Manga> = t.asSequence().filter { it != null }.map { convertResponse(it)!! }.toList()

    override fun convertResponse(it: MangaResponse?): Manga? {
        if (it == null) {
            return null
        }

        return Manga(
                it.id,
                it.name,
                it.nameRu,
                imageConverter.convertResponse(it.image),
                it.url.appendHostIfNeed(),
                it.type,
                it.status,
                it.volumes,
                it.chapters,
                it.dateAired,
                it.dateReleased
        )
    }
}