package com.gnoemes.shikimori.presentation.presenter.main

import moxy.InjectViewState
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.gnoemes.shikimori.domain.series.SeriesSyncInteractor
import com.gnoemes.shikimori.entity.common.domain.KeyScreen
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.presentation.presenter.base.BaseNavigationPresenter
import com.gnoemes.shikimori.presentation.view.main.MainView
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val _router: Router,
        private val interactor: SeriesSyncInteractor
) : BaseNavigationPresenter<MainView>() {

    override val router: Router
        get() = _router

    private val syncDisposable = CompositeDisposable()

    override fun initData() {
        onTabItemSelected(BottomScreens.RATES)
        startEpisodesSync()
    }

    private fun startEpisodesSync() {
        syncDisposable.add(
                interactor.startSync()
                        .subscribe({}, { FirebaseCrashlytics.getInstance().recordException(it) })
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        syncDisposable.clear()
    }

    fun onTabItemReselected(screenKey: String) {
        when (screenKey) {
            BottomScreens.RATES -> viewState.rateActionOrClearBackStack()
            BottomScreens.CALENDAR -> viewState.clearCalendarBackStack()
            BottomScreens.SEARCH -> viewState.searchActionOrClearSearchBackStack()
            BottomScreens.MAIN -> viewState.clearMainBackStack()
            BottomScreens.MORE -> viewState.clearMoreBackStack()
        }
    }

    fun onTabItemSelected(screenKey: String) {
        when (screenKey) {
            BottomScreens.RATES -> router.replaceScreen(KeyScreen(BottomScreens.RATES))
            BottomScreens.CALENDAR -> router.replaceScreen(KeyScreen(BottomScreens.CALENDAR))
            BottomScreens.SEARCH -> router.replaceScreen(KeyScreen(BottomScreens.SEARCH))
            BottomScreens.MAIN -> router.replaceScreen(KeyScreen(BottomScreens.MAIN))
            BottomScreens.MORE -> {
                viewState.clearMoreBackStack()
                router.replaceScreen(KeyScreen(BottomScreens.MORE))
            }
        }
    }
}