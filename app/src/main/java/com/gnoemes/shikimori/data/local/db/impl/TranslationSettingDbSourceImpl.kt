package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.TranslationSettingDbSource
import com.gnoemes.shikimori.data.local.db.table.TranslationSettingTable
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import com.pushtorefresh.storio3.sqlite.queries.Query
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TranslationSettingDbSourceImpl @Inject constructor(
        private val storIOSQLite: StorIOSQLite
) : TranslationSettingDbSource {

    override fun saveSetting(setting: TranslationSetting): Completable {
        val dao = TranslationSettingDao(setting.animeId, setting.episodeIndex, setting.lastAuthor, setting.lastType?.type)
        return storIOSQLite
                .put()
                .`object`(dao)
                .prepare()
                .asRxCompletable()
    }

    override fun getSetting(animeId: Long, episodeIndex: Int): Single<TranslationSetting> =
            storIOSQLite
                    .get()
                    .`object`(TranslationSettingDao::class.java)
                    .withQuery(Query.builder()
                            .table(TranslationSettingTable.TABLE)
                            .where("${TranslationSettingTable.COLUMN_ANIME_ID} = ? AND ${TranslationSettingTable.COLUMN_EPISODE_INDEX} = ?")
                            .whereArgs(animeId, episodeIndex)
                            .build()
                    ).prepare()
                    .asRxSingle()
                    .map { optional ->
                        if (optional.isPresent) optional.get().let { dao -> TranslationSetting(dao.animeId, dao.episodeIndex, dao.author, TranslationType.values().find { it.type == dao.type }) }
                        else getDefaultItem(animeId, episodeIndex)
                    }.onErrorReturnItem(getDefaultItem(animeId, episodeIndex))

    private fun getDefaultItem(animeId: Long, episodeIndex: Int): TranslationSetting = TranslationSetting(animeId, episodeIndex, null, null)
}