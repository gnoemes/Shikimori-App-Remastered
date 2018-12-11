package com.gnoemes.shikimori.presentation.view.base.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

abstract class BaseActivity<Presenter : BasePresenter<View>, View : BaseView> : BaseThemedActivity(),
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
        super.onCreate(savedInstanceState)
        setContentView(getLayoutActivity())
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        getNavigatorHolder().setNavigator(getNavigator())
    }

    override fun onPause() {
        getNavigatorHolder().removeNavigator()
        super.onPause()
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

