package com.gnoemes.shikimori.entity.series.data

import com.gnoemes.shikimori.data.local.db.table.EpisodeTable
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteColumn
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteCreator
import com.pushtorefresh.storio3.sqlite.annotations.StorIOSQLiteType

@StorIOSQLiteType(table = EpisodeTable.TABLE)
data class EpisodeDao @StorIOSQLiteCreator constructor(
        @StorIOSQLiteColumn(name = EpisodeTable.COLUMN_ANIME_ID, key = true) val animeId: Long,
        @StorIOSQLiteColumn(name = EpisodeTable.COLUMN_EPISODE_ID, key = true) val episodeId: Int,
        @StorIOSQLiteColumn(name = EpisodeTable.COLUMN_IS_WATCHED) val isWatched: Int?
)