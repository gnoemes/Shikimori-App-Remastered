package com.gnoemes.shikimori.presentation.presenter.character

import moxy.InjectViewState
import com.gnoemes.shikimori.domain.roles.CharacterInteractor
import com.gnoemes.shikimori.entity.app.domain.Constants
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.entity.roles.domain.CharacterDetails
import com.gnoemes.shikimori.presentation.presenter.base.BaseNetworkPresenter
import com.gnoemes.shikimori.presentation.presenter.character.converter.CharacterDetailsViewModelConverter
import com.gnoemes.shikimori.presentation.presenter.common.provider.CommonResourceProvider
import com.gnoemes.shikimori.presentation.view.character.CharacterView
import com.gnoemes.shikimori.utils.appendLoadingLogic
import javax.inject.Inject

@InjectViewState
class CharacterPresenter @Inject constructor(
        private val interactor: CharacterInteractor,
        private val resourceProvider: CommonResourceProvider,
        private val converter: CharacterDetailsViewModelConverter
) : BaseNetworkPresenter<CharacterView>() {
    var id: Long = Constants.NO_ID
    private lateinit var currentCharacter: CharacterDetails

    override fun initData() {
        loadCharacter()
    }

    private fun loadCharacter() =
            interactor.getDetails(id)
                    .appendLoadingLogic(viewState)
                    .doOnSuccess { currentCharacter = it }
                    .map(converter)
                    .subscribe(this::setData, this::processErrors)
                    .addToDisposables()

    private fun setData(items: List<Any>) {
        items.forEach {
            when (it) {
                is DetailsHeadSimpleItem -> viewState.setHead(it)
                is DetailsDescriptionItem -> viewState.setDescription(it)
                is Pair<*, *> -> when (it.first as DetailsContentType) {
                    DetailsContentType.SEYUS -> viewState.setSeyuContent(it.second as DetailsContentItem)
                    DetailsContentType.ANIMES -> viewState.setAnimeContent(it.second as DetailsContentItem)
                    DetailsContentType.MANGAS -> viewState.setMangaContent(it.second as DetailsContentItem)
                    else -> Unit
                }
            }
        }
    }


    fun onOpenInBrowser() {
        if (::currentCharacter.isInitialized) onOpenWeb(currentCharacter.url)
    }

    fun onOpenSource() {
        when {
            !::currentCharacter.isInitialized -> Unit
            currentCharacter.descriptionSource.isNullOrBlank() -> viewState.showSystemMessage(resourceProvider.emptyMessage)
            else -> onOpenWeb(currentCharacter.descriptionSource)
        }
    }
}
