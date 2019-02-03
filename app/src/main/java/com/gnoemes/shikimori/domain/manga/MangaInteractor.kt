package com.gnoemes.shikimori.domain.manga

import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import com.gnoemes.shikimori.entity.roles.domain.Character
import io.reactivex.Single

interface MangaInteractor {

    fun getDetails(id: Long): Single<MangaDetails>

    fun getRoles(id: Long): Single<List<Character>>

    fun getLinks(id: Long): Single<List<Link>>

    fun getSimilar(id: Long): Single<List<Manga>>

    fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>>

}