package com.gnoemes.shikimori.presentation.view.character

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.presentation.presenter.character.CharacterPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.character.adapter.CharacterAdapter
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

    private val characterAdapter by lazy { CharacterAdapter(imageLoader, settings, getPresenter()::onContentClicked) }

    companion object {
        fun newInstance(id: Long) = CharacterFragment().withArgs { putLong(AppExtras.ARGUMENT_CHARACTER_ID, id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton()
            setTitle(R.string.common_character)
            inflateMenu(R.menu.menu_character)
            setNavigationOnClickListener { getPresenter().onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_open_in_browser -> getPresenter().onOpenInBrowser()
                    R.id.item_source -> getPresenter().onOpenSource()
                }
                false
            }
        }

        with(characterRecyclerView) {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 5 }
            setHasFixedSize(true)
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

    override fun setData(it: List<Any>) {
        characterAdapter.bindItems(it)
    }


}