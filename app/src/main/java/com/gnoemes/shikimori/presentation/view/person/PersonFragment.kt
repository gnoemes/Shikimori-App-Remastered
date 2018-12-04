package com.gnoemes.shikimori.presentation.view.person

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.data.local.preference.SettingsSource
import com.gnoemes.shikimori.entity.app.domain.AppExtras
import com.gnoemes.shikimori.presentation.presenter.person.PersonPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseFragment
import com.gnoemes.shikimori.presentation.view.base.fragment.RouterProvider
import com.gnoemes.shikimori.presentation.view.person.adapter.PersonAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.ifNotNull
import com.gnoemes.shikimori.utils.images.ImageLoader
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_person.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import javax.inject.Inject

class PersonFragment : BaseFragment<PersonPresenter, PersonView>(), PersonView {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settings: SettingsSource

    @InjectPresenter
    lateinit var personPresenter: PersonPresenter

    @ProvidePresenter
    fun providePresenter(): PersonPresenter {
        personPresenter = presenterProvider.get()

        parentFragment.ifNotNull {
            personPresenter.localRouter = (parentFragment as RouterProvider).localRouter
        }

        arguments.ifNotNull {
            personPresenter.id = it.getLong(AppExtras.ARGUMENT_PERSON_ID)
        }

        return personPresenter
    }

    private val adapter by lazy { PersonAdapter(imageLoader, getPresenter()::onContentClicked) }

    companion object {
        fun newInstance(id: Long) = PersonFragment().withArgs { putLong(AppExtras.ARGUMENT_PERSON_ID, id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar?.apply {
            addBackButton()
            setTitle(R.string.common_person)
            inflateMenu(R.menu.menu_browser)
            setNavigationOnClickListener { getPresenter().onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.item_open_in_browser -> getPresenter().onOpenInBrowser()
                }
                false
            }
        }

        with(recyclerView) {
            adapter = this@PersonFragment.adapter
            layoutManager = LinearLayoutManager(context).apply { initialPrefetchItemCount = 5 }
            setHasFixedSize(true)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getPresenter(): PersonPresenter = personPresenter

    override fun getFragmentLayout(): Int = R.layout.fragment_person

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun setData(it: List<Any>) {
        adapter.bindItems(it)
    }
}