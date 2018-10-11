package com.gnoemes.shikimori.data.repository.related

import com.gnoemes.shikimori.data.network.AnimeApi
import com.gnoemes.shikimori.data.network.MangaApi
import com.gnoemes.shikimori.data.network.RanobeApi
import com.gnoemes.shikimori.data.repository.common.RelatedResponseConverter
import com.gnoemes.shikimori.entity.common.domain.Related
import io.reactivex.Single
import javax.inject.Inject

class RelatedRepositoryImpl @Inject constructor(
        private val animeApi: AnimeApi,
        private val mangaApi: MangaApi,
        private val ranobeApi: RanobeApi,
        private val converter: RelatedResponseConverter
) : RelatedRepository {

    override fun getAnime(animeId: Long): Single<List<Related>> = animeApi.getRelated(animeId).map(converter)

    override fun getManga(mangaId: Long): Single<List<Related>> = mangaApi.getRelated(mangaId).map(converter)

    override fun getRanobe(mangaId: Long): Single<List<Related>> = ranobeApi.getRelated(mangaId).map(converter)
}