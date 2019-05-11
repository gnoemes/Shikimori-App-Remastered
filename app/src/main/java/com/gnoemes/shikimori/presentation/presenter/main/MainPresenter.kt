package com.gnoemes.shikimori.presentation.presenter.main

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.entity.main.BottomScreens
import com.gnoemes.shikimori.presentation.presenter.base.BaseNavigationPresenter
import com.gnoemes.shikimori.presentation.view.main.MainView
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
        private val _router: Router
) : BaseNavigationPresenter<MainView>() {

    override val router: Router
        get() = _router

    override fun initData() {
        onTabItemSelected(BottomScreens.RATES)
    }

    fun onTabItemReselected(screenKey: String) {
        when (screenKey) {
            BottomScreens.RATES -> viewState.rateActionOrClearBackStack()
            BottomScreens.CALENDAR -> viewState.clearCalendarBackStack()
            BottomScreens.SEARCH -> viewState.clearSearchBackStack()
            BottomScreens.MAIN -> viewState.clearMainBackStack()
            BottomScreens.MORE -> viewState.clearMoreBackStack()
        }
    }

    fun onTabItemSelected(screenKey: String) {
        when (screenKey) {
            BottomScreens.RATES -> router.replaceScreen(BottomScreens.RATES)
            BottomScreens.CALENDAR -> router.replaceScreen(BottomScreens.CALENDAR)
            BottomScreens.SEARCH -> router.replaceScreen(BottomScreens.SEARCH)
            BottomScreens.MAIN -> router.replaceScreen(BottomScreens.MAIN)
            BottomScreens.MORE -> {
                viewState.clearMoreBackStack()
                router.replaceScreen(BottomScreens.MORE)
            }
        }
    }


}