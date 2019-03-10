package com.gnoemes.shikimori.presentation.view.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.presentation.presenter.search.FilterPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetInjectionDialogFragment
import com.gnoemes.shikimori.presentation.view.common.widget.spinner.MaterialSpinnerAdapter
import com.gnoemes.shikimori.presentation.view.search.filter.adapter.FilterAdapter
import com.gnoemes.shikimori.presentation.view.search.filter.genres.FilterGenresFragment
import com.gnoemes.shikimori.presentation.view.search.filter.seasons.FilterSeasonsFragment
import com.gnoemes.shikimori.utils.*
import kotlinx.android.synthetic.main.fragment_filter.*
import kotlinx.android.synthetic.main.layout_filter_bottom.*

class FilterFragment : BaseBottomSheetInjectionDialogFragment<FilterPresenter, FilterView>(), FilterView, FilterCallback {

    @InjectPresenter
    lateinit var filterPresenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter(): FilterPresenter = presenterProvider.get().apply {
        type = arguments?.getSerializable(TYPE_KEY) as? Type ?: Type.ANIME
        //copy of filters
        appliedFilters = HashMap(arguments?.getSerializable(FILTERS_KEY) as HashMap<String, MutableList<FilterItem>>)
    }

    companion object {
        private const val FILTERS_KEY = "FILTERS_KEY"
        private const val TYPE_KEY = "TYPE_KEY"
        private const val HINT_KEY = "HINT_KEY"
        private const val GENRES_TAG = "genresFilterDialog"
        private const val SEASONS_TAG = "seasonsFilterDialog"
        fun newInstance(type: Type, filters: HashMap<String, MutableList<FilterItem>>?) = FilterFragment()
                .withArgs {
                    putSerializable(TYPE_KEY, type)
                    putSerializable(FILTERS_KEY, filters)
                }
    }

    private val adapter by lazy { FilterAdapter(presenter::onFilterAction, presenter::onFilterSelected, presenter::onFilterInverted) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getDialogLayout(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (context!!.getDefaultSharedPreferences().getBoolean(HINT_KEY, false)) hintContainer.gone()
        else {
            hintContainer.onClick {
                putSetting(HINT_KEY, true)
                TransitionManager.beginDelayedTransition(appBarLayout, ChangeBounds())
                hintContainer.gone()
            }
        }

        with(toolbar) {
            setTitle(R.string.filters)
            addBackButton(R.drawable.ic_close) { onBackPressed() }
        }

        resetBtn.onClick { presenter.onResetClicked() }
        acceptBtn.onClick { presenter.onAcceptClicked() }

        sortSpinner.apply {
            popupWindow.setBackgroundDrawable(context.drawableAttr(R.attr.dialogCustomBackground))
            setCompoundDrawablesWithIntrinsicBounds(null, null, context.themeDrawable(R.drawable.ic_sort, R.attr.colorOnPrimary), null)
            setOnItemSelectedListener { _, _, _, item -> presenter.onSortChanged(item as FilterItem) }
        }

        with(list) {
            adapter = this@FilterFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            itemAnimator = null
        }
    }

    override fun onFiltersSelected(tag: String?, appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        val key = when (tag) {
            GENRES_TAG -> FilterType.GENRE.value
            SEASONS_TAG -> FilterType.SEASON.value
            else -> null
        }

        presenter.onNestedFilterCallback(key, appliedFilters)

    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_filter

    override val presenter: FilterPresenter
        get() = filterPresenter

    ///////////////////////////////////////////////////////////////////////////
    // MVP
    ///////////////////////////////////////////////////////////////////////////

    override fun showData(items: List<Any>) {
        adapter.bindItems(items)
    }

    override fun showGenresDialog(type: Type, filters: HashMap<String, MutableList<FilterItem>>) {
        val fragment = fragmentManager?.findFragmentByTag(GENRES_TAG)
        if (fragment == null) {
            val filter = FilterGenresFragment.newInstance(type, filters)
            filter.setTargetFragment(this, 43)
            postViewAction { filter.show(fragmentManager!!, GENRES_TAG) }
        }
    }

    override fun showSeasonsDialog(type: Type, filters: HashMap<String, MutableList<FilterItem>>) {
        val fragment = fragmentManager?.findFragmentByTag(SEASONS_TAG)
        if (fragment == null) {
            val filter = FilterSeasonsFragment.newInstance(type, filters)
            filter.setTargetFragment(this, 44)
            postViewAction { filter.show(fragmentManager!!, SEASONS_TAG) }
        }
    }

    override fun setResetEnabled(show: Boolean) {
        resetBtn.isEnabled = show
    }

    override fun setSortFilters(items: List<FilterItem>, selected: Int) {
        sortSpinner.setAdapter(MaterialSpinnerAdapter(context, items))
        sortSpinner.selectedIndex = selected
    }

    override fun onBackPressed() {
        dismiss()
    }

    override fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        (targetFragment as? FilterCallback)?.onFiltersSelected(tag, appliedFilters)
        onBackPressed()
    }

    override fun hideSoftInput() {
    }

    override fun setTitle(title: String) {
    }

    override fun setTitle(stringRes: Int) {
    }
}