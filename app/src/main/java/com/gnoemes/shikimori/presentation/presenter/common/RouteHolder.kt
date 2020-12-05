package com.gnoemes.shikimori.presentation.presenter.common

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.anime.domain.ScreenshotsNavigationData
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.auth.AuthType
import com.gnoemes.shikimori.entity.chronology.ChronologyNavigationData
import com.gnoemes.shikimori.entity.common.domain.CommonNavigationData
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.forum.domain.ForumType
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.entity.manga.presentation.MangaNavigationData
import com.gnoemes.shikimori.entity.rates.presentation.RateNavigationData
import com.gnoemes.shikimori.entity.search.presentation.SearchNavigationData
import com.gnoemes.shikimori.entity.series.presentation.EmbeddedPlayerNavigationData
import com.gnoemes.shikimori.entity.series.presentation.EpisodesNavigationData
import com.gnoemes.shikimori.entity.series.presentation.SeriesNavigationData
import com.gnoemes.shikimori.entity.user.presentation.UserHistoryNavigationData
import com.gnoemes.shikimori.presentation.view.anime.AnimeFragment
import com.gnoemes.shikimori.presentation.view.auth.AuthActivity
import com.gnoemes.shikimori.presentation.view.calendar.CalendarFragment
import com.gnoemes.shikimori.presentation.view.character.CharacterFragment
import com.gnoemes.shikimori.presentation.view.chronology.ChronologyFragment
import com.gnoemes.shikimori.presentation.view.clubs.UserClubsFragment
import com.gnoemes.shikimori.presentation.view.favorites.FavoritesFragment
import com.gnoemes.shikimori.presentation.view.friends.FriendsFragment
import com.gnoemes.shikimori.presentation.view.manga.MangaFragment
import com.gnoemes.shikimori.presentation.view.person.PersonFragment
import com.gnoemes.shikimori.presentation.view.player.embedded.EmbeddedPlayerActivity
import com.gnoemes.shikimori.presentation.view.player.web.WebPlayerActivity
import com.gnoemes.shikimori.presentation.view.rates.RateFragment
import com.gnoemes.shikimori.presentation.view.screenshots.ScreenshotsActivity
import com.gnoemes.shikimori.presentation.view.search.SearchFragment
import com.gnoemes.shikimori.presentation.view.series.SeriesFragment
import com.gnoemes.shikimori.presentation.view.series.episodes.EpisodesFragment
import com.gnoemes.shikimori.presentation.view.settings.SettingsActivity
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainFragment
import com.gnoemes.shikimori.presentation.view.similar.SimilarFragment
import com.gnoemes.shikimori.presentation.view.topic.details.TopicFragment
import com.gnoemes.shikimori.presentation.view.topic.list.TopicListFragment
import com.gnoemes.shikimori.presentation.view.user.UserFragment
import com.gnoemes.shikimori.presentation.view.userhistory.UserHistoryFragment
import com.gnoemes.shikimori.utils.toUri

object RouteHolder {

    fun createFragment(screenKey: String?, data: Any?): Fragment? {
        return when (screenKey) {
            BottomScreens.RATES -> RateFragment.newInstance(data as? RateNavigationData)
            BottomScreens.CALENDAR -> CalendarFragment.newInstance()
            BottomScreens.SEARCH -> SearchFragment.newInstance(data as? SearchNavigationData)
            BottomScreens.MAIN -> ShikimoriMainFragment.newInstance()
            BottomScreens.MORE -> UserFragment.newInstance()
            Screens.ANIME_DETAILS -> AnimeFragment.newInstance(data as Long)
            Screens.MANGA_DETAILS -> MangaFragment.newInstance(data as MangaNavigationData)
            Screens.CHARACTER_DETAILS -> CharacterFragment.newInstance(data as Long)
            Screens.PERSON_DETAILS -> PersonFragment.newInstance(data as Long)
            Screens.TOPIC_DETAILS -> TopicFragment.newInstance(data as Long)
            Screens.USER_DETAILS -> UserFragment.newInstance(data as Long)
            Screens.TOPICS -> TopicListFragment.newInstance(data as ForumType)
            Screens.EPISODES -> EpisodesFragment.newInstance(data as EpisodesNavigationData)
            Screens.SERIES -> SeriesFragment.newInstance(data as SeriesNavigationData)
            Screens.USER_FRIENDS -> FriendsFragment.newInstance(data as Long)
            Screens.USER_CLUBS -> UserClubsFragment.newInstance(data as Long)
            Screens.USER_HISTORY -> UserHistoryFragment.newInstance(data as UserHistoryNavigationData)
            Screens.USER_FAVORITES -> FavoritesFragment.newInstance(data as Long)
            Screens.SIMILAR -> SimilarFragment.newInstance(data as CommonNavigationData)
            Screens.CHRONOLOGY -> ChronologyFragment.newInstance(data as ChronologyNavigationData)
            else -> null
        }
    }

    fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
        return when (screenKey) {
            Screens.AUTHORIZATION -> AuthActivity.shikimoriAuth(context, data as AuthType)
            //TODO check settings to open in internal on external browser
            Screens.WEB -> Intent(Intent.ACTION_VIEW, Uri.parse(data as String))
            Screens.SETTINGS -> Intent(context, SettingsActivity::class.java)
            Screens.WEB_PLAYER -> Intent(context, WebPlayerActivity::class.java).apply { putExtra(AppExtras.ARGUMENT_URL, data as? String) }
            Screens.EMBEDDED_PLAYER -> Intent(context, EmbeddedPlayerActivity::class.java).apply { putExtra(AppExtras.ARGUMENT_PLAYER_DATA, data as EmbeddedPlayerNavigationData) }
            Screens.EXTERNAL_PLAYER -> {
                Intent(Intent.ACTION_VIEW, data?.toString()?.toUri()).apply {
                    setDataAndType(data?.toString()?.toUri(), "video/mp4")
                    putExtra("headers", arrayOf("User-Agent", "sap"))
                }
            }
            Screens.SHARE -> Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, data?.toString()) }, context!!.getString(R.string.common_share))
            Screens.SCREENSHOTS -> ScreenshotsActivity.newIntent(context, data as ScreenshotsNavigationData)
            else -> null
        }
    }
}