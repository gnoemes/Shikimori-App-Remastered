package com.gnoemes.shikimori.presentation.view.character

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsContentType
import com.gnoemes.shikimori.entity.common.presentation.DetailsDescriptionItem
import com.gnoemes.shikimori.entity.common.presentation.DetailsHeadSimpleItem
import com.gnoemes.shikimori.presentation.presenter.character.CharacterPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.common.adapter.content.ContentAdapter
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsContentViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsDescriptionViewHolder
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsHeadSimpleViewHolder
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_character.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class CharacterFragment : BaseFragment<CharacterPresenter, CharacterView>(), CharacterView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settings: SettingsSource

    @InjectPresenter
    lateinit var characterPresenter: CharacterPresenter

    @ProvidePresenter
    fun providePresenter(): CharacterPresenter {
        characterPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            characterPresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            characterPresenter.id = it.getLong(AppExtras.ARGUMENT_CHARACTER_ID)
        }

        return characterPresenter
    }

    private lateinit var headHolder: DetailsHeadSimpleViewHolder
    private lateinit var descriptionHolder: DetailsDescriptionViewHolder
    private val contentHolders = HashMap<DetailsContentType, DetailsContentViewHolder>()

    private val seyuAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked) }
    private val animeAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked) }
    private val mangaAdapter by lazy { ContentAdapter(imageLoader, getPresenter()::onContentClicked) }


    companion object {
        fun newInstance(id: Long) = CharacterFragment().withArgs { putLong(AppExtras.ARGUMENT_CHARACTER_ID, id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton { getPresenter().onBackPressed() }
            setTitle(R.string.common_character)
            inflateMenu(R.menu.menu_character)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_open_in_browser -> getPresenter().onOpenInBrowser()
                    R.id.item_source -> getPresenter().onOpenSource()
                }
                false
            }
        }

        headHolder = DetailsHeadSimpleViewHolder(headLayout, imageLoader)
        descriptionHolder = DetailsDescriptionViewHolder(descriptionLayout)

        contentHolders.apply {
            put(DetailsContentType.SEYUS, DetailsContentViewHolder(seyuLayout, seyuAdapter))
            put(DetailsContentType.ANIMES, DetailsContentViewHolder(animeLayout, animeAdapter))
            put(DetailsContentType.MANGAS, DetailsContentViewHolder(mangaLayout, mangaAdapter))
        }


    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): CharacterPresenter = characterPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_character

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setHead(item: DetailsHeadSimpleItem) {
        headHolder.bind(item)
    }

    override fun setDescription(item: DetailsDescriptionItem) {
        descriptionHolder.bind(item)
    }

    override fun setContent(type: DetailsContentType, item: DetailsContentItem) {
        contentHolders[type]?.bind(type, item)
    }

    override fun onShowLoading()  = Unit
    override fun onHideLoading()  = Unit
}