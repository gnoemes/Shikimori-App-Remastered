package com.gnoemes.shikimori.presentation.view.player.embedded

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.series.domain.Track
import com.gnoemes.shikimori.presentation.presenter.player.EmbeddedPlayerPresenter
import com.gnoemes.shikimori.presentation.view.base.activity.BaseActivity
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

class EmbeddedPlayerActivity : BaseActivity<EmbeddedPlayerPresenter, EmbeddedPlayerView>(), EmbeddedPlayerView {

    @InjectPresenter
    lateinit var playerPresenter: EmbeddedPlayerPresenter

    @ProvidePresenter
    fun providePresenter(): EmbeddedPlayerPresenter =
            presenterProvider.get()
                    .apply { navigationData = intent.getParcelableExtra(AppExtras.ARGUMENT_PLAYER_DATA) }

    @Inject
    lateinit var localNavigatorHolder: NavigatorHolder


    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getLayoutActivity(): Int = R.layout.activity_embedded_player

    override fun getNavigator(): Navigator = Navigator { }

    override fun getNavigatorHolder(): NavigatorHolder = localNavigatorHolder

    override val presenter: EmbeddedPlayerPresenter
        get() = playerPresenter

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setTitle(title: String) {
    }

    override fun setEpisodeSubtitle(currentEpisode: Int) {
    }

    override fun enableNextButton() {
    }

    override fun disableNextButton() {
    }

    override fun enablePrevButton() {
    }

    override fun disablePrevButton() {
    }

    override fun showMessage(s: String, exit: Boolean) {
    }

    override fun selectTrack(currentTrack: Int) {
    }

    override fun playVideo(it: Track, needReset: Boolean) {
    }
}