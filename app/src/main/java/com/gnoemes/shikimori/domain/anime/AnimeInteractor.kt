package com.gnoemes.shikimori.domain.anime

import com.gnoemes.shikimori.entity.anime.domain.Anime
import com.gnoemes.shikimori.entity.anime.domain.AnimeDetails
import com.gnoemes.shikimori.entity.anime.domain.Screenshot
import com.gnoemes.shikimori.entity.common.domain.FranchiseNode
import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.roles.domain.Character
import io.reactivex.Single

interface AnimeInteractor {

    fun getDetails(id: Long): Single<AnimeDetails>

    fun getRoles(id: Long): Single<List<Character>>

    fun getLinks(id: Long): Single<List<Link>>

    fun getSimilar(id: Long): Single<List<Anime>>

    fun getFranchiseNodes(id: Long): Single<List<FranchiseNode>>

    fun getScreenshots(id: Long): Single<List<Screenshot>>

}