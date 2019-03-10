package com.gnoemes.shikimori.presentation.view.search.filter.genres

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.presenter.search.FilterGenresPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetInjectionDialogFragment
import com.gnoemes.shikimori.presentation.view.search.filter.FilterCallback
import com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter.FilterGenreAdapter
import com.gnoemes.shikimori.utils.*
import kotlinx.android.synthetic.main.fragment_filter_genres.*
import kotlinx.android.synthetic.main.layout_filter_genres_toolbar.*

class FilterGenresFragment : BaseBottomSheetInjectionDialogFragment<FilterGenresPresenter, FilterGenresView>(), FilterGenresView {

    @InjectPresenter
    lateinit var filterPresenter: FilterGenresPresenter

    @ProvidePresenter
    fun providePresenter(): FilterGenresPresenter = presenterProvider.get().apply {
        type = arguments?.getSerializable(TYPE_KEY) as? Type ?: Type.ANIME
        //copy of filters
        appliedFilters = HashMap(arguments?.getSerializable(FILTERS_KEY) as HashMap<String, MutableList<FilterItem>>)
    }

    companion object {
        private const val FILTERS_KEY = "FILTERS_KEY"
        private const val TYPE_KEY = "TYPE_KEY"
        fun newInstance(type: Type, filters: HashMap<String, MutableList<FilterItem>>?) = FilterGenresFragment()
                .withArgs {
                    putSerializable(TYPE_KEY, type)
                    putSerializable(FILTERS_KEY, filters)
                }
    }

    private val adapter by lazy { FilterGenreAdapter(presenter::onFilterInverted, presenter::onFilterSelected) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = Point().let { activity?.windowManager?.defaultDisplay?.getSize(it);it }.x - context.dimenAttr(android.R.attr.actionBarSize)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getDialogLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbar) {
            setTitle(R.string.filters_genres)
            addBackButton(R.drawable.ic_close) { onBackPressed() }
        }

        with(recyclerView) {
            adapter = this@FilterGenresFragment.adapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        clearBtn.onClick { presenter.onResetClicked() }
        acceptBtn.onClick { presenter.onAcceptClicked() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override val presenter: FilterGenresPresenter
        get() = filterPresenter

    override fun getDialogLayout(): Int = R.layout.fragment_filter_genres

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<Any>) {
        adapter.bindItems(items)
    }

    override fun setResetEnabled(show: Boolean) {
        clearBtn.visibleIf { show }
    }

    override fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        (targetFragment as? FilterCallback)?.onFiltersSelected(appliedFilters)
        onBackPressed()
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun hideSoftInput() {
    }

    override fun setTitle(title: String) {
    }

    override fun setTitle(stringRes: Int) {
    }
}