package com.gnoemes.shikimori.data.repository.ranobe

import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import io.reactivex.Single

interface RanobeRepository {

    fun getDetails(id: Long): Single<MangaDetails>

    fun getRoles(id: Long): Single<Roles>

    fun getLinks(id: Long): Single<List<Link>>

    fun getSimilar(id: Long): Single<List<Manga>>

    fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>>
}