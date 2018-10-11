package com.gnoemes.shikimori.domain.anime

import com.gnoemes.shikimori.data.repository.anime.AnimeRepository
import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class AnimeInteractorImpl @Inject constructor(
        private val repository: AnimeRepository
) : AnimeInteractor {

    override fun getDetails(id: Long): Single<AnimeDetails> =
            repository.getDetails(id)
                    .applyErrorHandlerAndSchedulers()

    override fun getLinks(id: Long): Single<List<Link>> =
            repository.getLinks(id)
                    .applyErrorHandlerAndSchedulers()

    override fun getSimilar(id: Long): Single<List<Anime>> =
            repository.getSimilar(id).applyErrorHandlerAndSchedulers()

    override fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>> =
            repository.getFranchiseNodes(id)
                    .applyErrorHandlerAndSchedulers()

    override fun getScreenshots(id: Long): Single<List<Screenshot>> =
            repository.getScreenshots(id)
                    .applyErrorHandlerAndSchedulers()
}