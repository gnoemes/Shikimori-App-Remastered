package com.gnoemes.shikimori.presentation.view.search.filter

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentFilterBinding
import com.gnoemes.shikimori.databinding.LayoutFilterBottomBinding
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.search.domain.FilterType
import com.gnoemes.shikimori.presentation.presenter.search.FilterPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetInjectionDialogFragment
import com.gnoemes.shikimori.presentation.view.common.fragment.ListDialogFragment
import com.gnoemes.shikimori.presentation.view.search.filter.adapter.FilterAdapter
import com.gnoemes.shikimori.presentation.view.search.filter.genres.FilterGenresFragment
import com.gnoemes.shikimori.presentation.view.search.filter.seasons.FilterSeasonsFragment
import com.gnoemes.shikimori.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FilterFragment : BaseBottomSheetInjectionDialogFragment<FilterPresenter, FilterView>(), FilterView, FilterCallback, ListDialogFragment.DialogCallback {

    @InjectPresenter
    lateinit var filterPresenter: FilterPresenter

    @ProvidePresenter
    fun providePresenter(): FilterPresenter = presenterProvider.get().apply {
        type = arguments?.getSerializable(TYPE_KEY) as? Type ?: Type.ANIME
        //copy of filters
        @Suppress("DEPRECATION")
        val raw = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable(FILTERS_KEY, HashMap::class.java)
        } else {
            arguments?.getSerializable(FILTERS_KEY)
        } as? HashMap<String, MutableList<FilterItem>>
        appliedFilters = HashMap(raw ?: HashMap())
    }

    companion object {
        private const val FILTERS_KEY = "FILTERS_KEY"
        private const val TYPE_KEY = "TYPE_KEY"
        private const val HINT_KEY = "HINT_KEY"
        private const val GENRES_TAG = "genresFilterDialog"
        private const val SEASONS_TAG = "seasonsFilterDialog"
        const val GENRES_RESULT_KEY = "genres_result_key"
        const val SEASONS_RESULT_KEY = "seasons_result_key"
        const val FILTER_RESULT_KEY = "filter_result_key"
        const val RESULT_TAG_KEY = "result_tag"
        const val RESULT_FILTERS_KEY = "result_filters"
        fun newInstance(type: Type, filters: HashMap<String, MutableList<FilterItem>>?) = FilterFragment()
                .withArgs {
                    putSerializable(TYPE_KEY, type)
                    putSerializable(FILTERS_KEY, filters)
                }
    }

    private val adapter by lazy { FilterAdapter(presenter::onFilterAction, presenter::onFilterSelected, presenter::onFilterInverted) }
    private var _binding: FragmentFilterBinding? = null
    private val binding: FragmentFilterBinding? get() = _binding
    private var _bottomBinding: LayoutFilterBottomBinding? = null
    private val bottomBinding: LayoutFilterBottomBinding? get() = _bottomBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        _bottomBinding = LayoutFilterBottomBinding.bind(_binding!!.root.findViewById(R.id.included_layout_filter_bottom))
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return
        val bb = _bottomBinding ?: return

        if (context!!.getDefaultSharedPreferences().getBoolean(HINT_KEY, false)) b.hintContainer.gone()
        else {
            b.hintContainer.onClick {
                putSetting(HINT_KEY, true)
                TransitionManager.beginDelayedTransition(b.appBarLayout, ChangeBounds())
                b.hintContainer.gone()
            }
        }

        with(b.toolbar) {
            setTitle(R.string.filters)
            addBackButton(R.drawable.ic_close) { onBackPressed() }
        }

        b.resetBtn.onClick { presenter.onResetClicked() }
        bb.acceptBtn.onClick { presenter.onAcceptClicked() }
        bb.sortBtn.onClick { presenter.onSortClicked() }

        with(b.list) {
            adapter = this@FilterFragment.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            itemAnimator = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _bottomBinding = null
    }

    override fun onFiltersSelected(tag: String?, appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        val key = when (tag) {
            GENRES_TAG -> FilterType.GENRE.value
            SEASONS_TAG -> FilterType.SEASON.value
            else -> null
        }

        presenter.onNestedFilterCallback(key, appliedFilters)
    }

    override fun dialogItemCallback(tag: String?, url: String) {
        presenter.onSortChanged(Gson().fromJson(url, FilterItem::class.java))
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
        val fragment = parentFragmentManager.findFragmentByTag(GENRES_TAG)
        if (fragment == null) {
            val filter = FilterGenresFragment.newInstance(type, filters)
            parentFragmentManager.setFragmentResultListener(GENRES_RESULT_KEY, this) { _, bundle ->
                val filterTag = bundle.getString(RESULT_TAG_KEY)
                val filtersJson = bundle.getString(RESULT_FILTERS_KEY)
                val typeToken = object : TypeToken<HashMap<String, MutableList<FilterItem>>>() {}.type
                val appliedFilters: HashMap<String, MutableList<FilterItem>> = Gson().fromJson(filtersJson, typeToken)
                onFiltersSelected(filterTag, appliedFilters)
            }
            postViewAction { filter.show(parentFragmentManager, GENRES_TAG) }
        }
    }

    override fun showSeasonsDialog(type: Type, filters: HashMap<String, MutableList<FilterItem>>) {
        val fragment = parentFragmentManager.findFragmentByTag(SEASONS_TAG)
        if (fragment == null) {
            val filter = FilterSeasonsFragment.newInstance(type, filters)
            parentFragmentManager.setFragmentResultListener(SEASONS_RESULT_KEY, this) { _, bundle ->
                val filterTag = bundle.getString(RESULT_TAG_KEY)
                val filtersJson = bundle.getString(RESULT_FILTERS_KEY)
                val typeToken = object : TypeToken<HashMap<String, MutableList<FilterItem>>>() {}.type
                val appliedFilters: HashMap<String, MutableList<FilterItem>> = Gson().fromJson(filtersJson, typeToken)
                onFiltersSelected(filterTag, appliedFilters)
            }
            postViewAction { filter.show(parentFragmentManager, SEASONS_TAG) }
        }
    }

    override fun setResetEnabled(show: Boolean) {
        _binding?.resetBtn?.isEnabled = show
    }

    override fun showSortFilters(items: List<FilterItem>) {
        val gson = Gson()
        val dialog = ListDialogFragment.newInstance()
        dialog.apply {
            setItems(items.map { Pair(it.localizedText!!, gson.toJson(it)) })
        }.show(childFragmentManager, "SortsTag")
    }

    override fun setSortFilterText(text: String) {
        _bottomBinding?.sortBtn?.text = text
    }

    override fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        val bundle = Bundle().apply {
            putString(RESULT_TAG_KEY, tag)
            putString(RESULT_FILTERS_KEY, Gson().toJson(appliedFilters))
        }
        parentFragmentManager.setFragmentResult(FILTER_RESULT_KEY, bundle)
        onBackPressed()
    }
}