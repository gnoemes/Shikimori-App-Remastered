package com.gnoemes.shikimori.presentation.presenter.base

import com.gnoemes.shikimori.entity.app.domain.AnalyticEvent
import com.gnoemes.shikimori.entity.common.domain.Screens
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.manga.presentation.MangaNavigationData
import com.gnoemes.shikimori.entity.series.domain.PlayerType
import com.gnoemes.shikimori.presentation.view.base.activity.BaseView
import ru.terrakok.cicerone.Router


abstract class BaseNavigationPresenter<View : BaseView> : BaseAnalyticPresenter<View>() {

    lateinit var localRouter: Router

    override val router: Router
        get() = if (::localRouter.isInitialized) localRouter else Router()

    override fun onBackPressed() = router.exit()

    open fun onAnimeClicked(id: Long) {
        router.navigateTo(Screens.ANIME_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_ANIME)
    }

    open fun onMangaClicked(id: Long) {
        router.navigateTo(Screens.MANGA_DETAILS, MangaNavigationData(id, Type.MANGA))
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_MANGA)
    }

    open fun onRanobeClicked(id: Long){
        router.navigateTo(Screens.MANGA_DETAILS, MangaNavigationData(id, Type.RANOBE))
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_RANOBE)
    }

    open fun onCharacterClicked(id: Long) {
        router.navigateTo(Screens.CHARACTER_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_CHARACTER)
    }

    open fun onUserClicked(id: Long) {
        router.navigateTo(Screens.USER_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_USER)
    }

    open fun onPersonClicked(id: Long) {
        router.navigateTo(Screens.PERSON_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_PERSON)
    }

    open fun onTopicClicked(id: Long){
        router.navigateTo(Screens.TOPIC_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_TOPIC)
    }

    open fun onClubClicked(id: Long){
        router.navigateTo(Screens.CLUB_DETAILS, id)
        logEvent(AnalyticEvent.NAVIGATION_DETAILS_CLUB)
    }

    open fun onOpenWeb(url: String?) {
        router.navigateTo(Screens.WEB, url)
        logEvent(AnalyticEvent.NAVIGATION_WEB)
    }

    fun onContentClicked(type: Type, id: Long) {
        when (type) {
            Type.ANIME -> onAnimeClicked(id)
            Type.MANGA -> onMangaClicked(id)
            Type.RANOBE -> onRanobeClicked(id)
            Type.CHARACTER -> onCharacterClicked(id)
            Type.USER -> onUserClicked(id)
            Type.PERSON -> onPersonClicked(id)
            Type.TOPIC -> onTopicClicked(id)
            Type.CLUB -> onClubClicked(id)
            else -> Unit
        }
    }

    open fun openPlayer(playerType: PlayerType, payload: Any?) = when (playerType) {
        PlayerType.EMBEDDED -> openEmbeddedPlayer(payload)
        PlayerType.WEB -> openWebPlayer(payload)
        PlayerType.EXTERNAL -> openExternalPlayer(payload)
    }

    open fun openEmbeddedPlayer(payload: Any?) {
        router.navigateTo(Screens.EMBEDDED_PLAYER, payload)
        logEvent(AnalyticEvent.PLAYER_OPENED_EMBEDDED)
    }

    open fun openWebPlayer(payload: Any?) {
        router.navigateTo(Screens.WEB_PLAYER, payload)
        logEvent(AnalyticEvent.PLAYER_OPENED_WEB)
    }

    open fun openExternalPlayer(payload: Any?) {
        router.navigateTo(Screens.EXTERNAL_PLAYER, payload)
        logEvent(AnalyticEvent.PLAYER_OPENED_EXTERNAL)
    }

}