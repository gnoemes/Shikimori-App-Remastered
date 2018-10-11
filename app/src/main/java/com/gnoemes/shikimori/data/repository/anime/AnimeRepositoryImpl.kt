package com.gnoemes.shikimori.data.repository.anime

import com.gnoemes.shikimori.data.local.db.AnimeRateSyncDbSource
import com.gnoemes.shikimori.data.local.db.EpisodeDbSource
import com.gnoemes.shikimori.data.network.AnimeApi
import com.gnoemes.shikimori.data.repository.anime.converter.AnimeDetailsResponseConverter
import com.gnoemes.shikimori.data.repository.common.AnimeResponseConverter
import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter
import com.gnoemes.shikimori.data.repository.common.LinkResponseConverter
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.utils.appendHostIfNeed
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.joda.time.DateTimeComparator
import javax.inject.Inject

class AnimeRepositoryImpl @Inject constructor(
        private val api: AnimeApi,
        private val syncDbSource: AnimeRateSyncDbSource,
        private val episodeDbSource: EpisodeDbSource,
        private val linkConverter: LinkResponseConverter,
        private val animeConverter: AnimeResponseConverter,
        private val franchiseConverter: FranchiseResponseConverter,
        private val detailsConverter: AnimeDetailsResponseConverter
) : AnimeRepository {

    override fun getDetails(id: Long): Single<AnimeDetails> =
            Single.zip(api.getDetails(id), api.getRoles(id), BiFunction(detailsConverter::convertResponse))
                    .flatMap { syncRate(it).toSingleDefault(it) }

    override fun getLinks(id: Long): Single<List<Link>> =
            api.getLinks(id)
                    .map(linkConverter)

    override fun getSimilar(id: Long): Single<List<Anime>> =
            api.getSimilar(id)
                    .map(animeConverter)

    override fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>> =
            api.getFranchise(id)
                    .map(franchiseConverter)
                    .map { it.sortedWith(DateTimeComparator.getInstance()) }

    override fun getScreenshots(id: Long): Single<List<Screenshot>> =
            api.getScreenshots(id)
                    .map { list -> list.map { Screenshot(it.original?.appendHostIfNeed(), it.preview?.appendHostIfNeed()) } }

    override fun getLocalWatchedAnimeIds(): Single<LinkedHashSet<Long>> =
            episodeDbSource.getWatchedAnimeIds()
                    .map { LinkedHashSet(it) }

    private fun syncRate(details: AnimeDetails): Completable =
            Single.fromCallable { details }
                    .filter { details.userRate != null }
                    .flatMapCompletable { syncDbSource.saveRate(it.userRate!!) }

}