package com.gnoemes.shikimori.data.local.db.impl

import com.gnoemes.shikimori.data.local.db.TranslationSettingDbSource
import com.gnoemes.shikimori.data.local.db.dao.TranslationSettingRoomDao
import com.gnoemes.shikimori.entity.series.data.TranslationSettingDao
import com.gnoemes.shikimori.entity.series.domain.TranslationSetting
import com.gnoemes.shikimori.entity.series.domain.TranslationType
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class TranslationSettingDbSourceImpl @Inject constructor(
        private val translationSettingDao: TranslationSettingRoomDao
) : TranslationSettingDbSource {

    override fun saveSetting(setting: TranslationSetting): Completable {
        val dao = TranslationSettingDao(setting.animeId, setting.lastAuthor, setting.lastType?.type)
        return translationSettingDao.insert(dao)
    }

    override fun getSetting(animeId: Long): Single<TranslationSetting> =
            translationSettingDao.getSetting(animeId)
                    .map { dao -> TranslationSetting(dao.animeId, dao.author, TranslationType.values().find { it.type == dao.type }) }
                    .toSingle(getDefaultItem(animeId))
                    .onErrorReturnItem(getDefaultItem(animeId))

    private fun getDefaultItem(animeId: Long): TranslationSetting = TranslationSetting(animeId, null, null)
}
