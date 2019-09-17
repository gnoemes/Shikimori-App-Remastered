package com.gnoemes.shikimori.domain.chronology

import com.gnoemes.shikimori.entity.chronology.ChronologyItem
import com.gnoemes.shikimori.entity.chronology.ChronologyType
import io.reactivex.Single

interface ChronologyInteractor {
    fun getAnimes(id: Long, franchiseName: String?, type : ChronologyType): Single<List<ChronologyItem>>
    fun getMangas(id: Long, franchiseName: String?, type : ChronologyType): Single<List<ChronologyItem>>
    fun getRanobes(id: Long, franchiseName: String?, type : ChronologyType): Single<List<ChronologyItem>>
}