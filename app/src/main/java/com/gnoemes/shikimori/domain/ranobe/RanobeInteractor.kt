package com.gnoemes.shikimori.domain.ranobe

import com.gnoemes.shikimori.entity.common.domain.Link
import com.gnoemes.shikimori.entity.common.domain.Roles
import com.gnoemes.shikimori.entity.manga.domain.Manga
import com.gnoemes.shikimori.entity.manga.domain.MangaDetails
import io.reactivex.Single

interface RanobeInteractor {

    fun getDetails(id: Long): Single<MangaDetails>

    fun getRoles(id: Long): Single<Roles>

    fun getLinks(id: Long): Single<List<Link>>

    fun getSimilar(id: Long): Single<List<Manga>>
}