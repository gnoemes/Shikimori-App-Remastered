package com.gnoemes.shikimori.presentation.presenter.shikimorimain

import com.arellomobile.mvp.InjectViewState
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.view.shikimorimain.ShikimoriMainView
import javax.inject.Inject

@InjectViewState
class ShikimoriMainPresenter @Inject constructor(

) : BaseNetworkPresenter<ShikimoriMainView>()