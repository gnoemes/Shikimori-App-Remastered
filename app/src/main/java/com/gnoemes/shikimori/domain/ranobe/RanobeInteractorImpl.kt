package com.gnoemes.shikimori.domain.ranobe

import com.gnoemes.shikimori.data.repository.ranobe.RanobeRepository
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.utils.applyErrorHandlerAndSchedulers
import io.reactivex.Single
import javax.inject.Inject

class RanobeInteractorImpl @Inject constructor(
        private val repository: RanobeRepository
) : RanobeInteractor {

    override fun getDetails(id: Long): Single<MangaDetails> = repository.getDetails(id).applyErrorHandlerAndSchedulers()

    override fun getRoles(id: Long): Single<Roles> = repository.getRoles(id).applyErrorHandlerAndSchedulers()

    override fun getLinks(id: Long): Single<List<Link>> = repository.getLinks(id).applyErrorHandlerAndSchedulers()

    override fun getSimilar(id: Long): Single<List<Manga>> = repository.getSimilar(id).applyErrorHandlerAndSchedulers()

    override fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>> = repository.getFranchiseNodes(id).applyErrorHandlerAndSchedulers()

}