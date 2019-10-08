package com.gnoemes.shikimori.data.repository.manga

import com.gnoemes.shikimori.data.local.db.MangaRateSyncDbSource
import com.gnoemes.shikimori.data.network.MangaApi
import com.gnoemes.shikimori.data.repository.common.FranchiseResponseConverter
import com.gnoemes.shikimori.data.repository.common.LinkResponseConverter
import com.gnoemes.shikimori.data.repository.common.MangaResponseConverter
import com.gnoemes.shikimori.data.repository.common.RolesResponseConverter
import com.gnoemes.shikimori.data.repository.manga.converter.MangaDetailsResponseConverter
import com.gnoemes.shikimori.entity.common.domain.Franchise
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class MangaRepositoryImpl @Inject constructor(
        private val api: MangaApi,
        private val syncDbSource: MangaRateSyncDbSource,
        private val detailsConverter: MangaDetailsResponseConverter,
        private val linkConverter: LinkResponseConverter,
        private val franchiseConverter: FranchiseResponseConverter,
        private val mangaConverter: MangaResponseConverter,
        private val rolesConverter: RolesResponseConverter
) : MangaRepository {

    override fun getDetails(id: Long): Single<MangaDetails> =
            api.getDetails(id)
                    .map(detailsConverter)
                    .flatMap { syncRate(it).toSingleDefault(it) }

    override fun getRoles(id: Long): Single<Roles> =
            api.getRoles(id)
                    .map(rolesConverter)

    override fun getLinks(id: Long): Single<List<Link>> =
            api.getLinks(id)
                    .map(linkConverter)

    override fun getSimilar(id: Long): Single<List<Manga>> =
            api.getSimilar(id)
                    .map(mangaConverter)

    override fun getFranchise(id: Long): Single<Franchise> =
            api.getFranchise(id)
                    .map(franchiseConverter)

    private fun syncRate(details: MangaDetails): Completable =
            Single.fromCallable { details }
                    .filter { details.userRate != null }
                    .flatMapCompletable { syncDbSource.saveRate(it.userRate!!) }
}