package com.gnoemes.shikimori.presentation.view.base.activity

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.afollestad.aesthetic.Aesthetic
import com.afollestad.aesthetic.AutoSwitchMode
import com.afollestad.aesthetic.BottomNavBgMode
import com.afollestad.aesthetic.BottomNavIconTextMode
import com.arellomobile.mvp.MvpAppCompatActivity
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.di.base.modules.BaseActivityModule
import com.gnoemes.shikimori.presentation.presenter.base.BasePresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BackButtonListener
import com.gnoemes.shikimori.utils.inputMethodManager
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

abstract class BaseActivity<Presenter : BasePresenter<View>, View : BaseView> : MvpAppCompatActivity(),
        HasSupportFragmentInjector, BaseView {

    @Inject
    lateinit var presenterProvider: Provider<Presenter>

    @Inject
    @field:Named(BaseActivityModule.ACTIVITY_FRAGMENT_MANAGER)
    lateinit var fragmentManager: FragmentManager

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        Aesthetic.attach(this)
        super.onCreate(savedInstanceState)
        configureAesthetic()
        setContentView(getLayoutActivity())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        getNavigatorHolder().setNavigator(getNavigator())
    }

    override fun onPause() {
        getNavigatorHolder().removeNavigator()
        Aesthetic.pause(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Aesthetic.resume(this)
    }

    private fun configureAesthetic() {
        if (Aesthetic.isFirstTime) {
            Aesthetic.config {
                colorPrimaryRes(R.color.colorPrimary)
                colorPrimaryDarkRes(R.color.colorPrimaryDark)
                colorAccentRes(R.color.colorAccent)
                colorWindowBackgroundRes(R.color.colorBackground)
                colorStatusBarAuto()
                colorNavigationBarAuto()
                bottomNavigationBackgroundMode(BottomNavBgMode.PRIMARY)
                bottomNavigationIconTextMode(BottomNavIconTextMode.SELECTED_ACCENT)
                textColorPrimaryRes(R.color.colorSecondaryInverse)
                textColorPrimaryInverseRes(R.color.colorSecondary)
                textColorSecondaryRes(R.color.colorSecondary)
                textColorSecondaryInverseRes(R.color.colorSecondaryInverse)
                lightStatusBarMode(AutoSwitchMode.AUTO)
                colorIconTitleActiveRes(R.color.colorAccent)
                colorIconTitleInactiveRes(R.color.black)
                colorCardViewBackgroundRes(R.color.colorBackground)
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    @LayoutRes
    protected abstract fun getLayoutActivity(): Int

    protected abstract fun getNavigator(): Navigator

    protected abstract fun getNavigatorHolder(): NavigatorHolder

    protected abstract val presenter: Presenter

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun hideSoftInput() {
        inputMethodManager()?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    override fun onShowLoading() = Unit

    override fun onHideLoading() = Unit

    override fun onBackPressed() {
        val fragment = fragmentManager.findFragmentById(R.id.activity_container)
        if (fragment != null
                && fragment is BackButtonListener
                && (fragment as BackButtonListener).onBackPressed())
            return
        else presenter.onBackPressed()
    }
}

