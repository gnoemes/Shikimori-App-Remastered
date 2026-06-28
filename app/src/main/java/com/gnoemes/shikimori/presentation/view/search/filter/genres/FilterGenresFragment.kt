package com.gnoemes.shikimori.presentation.view.search.filter.genres

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentFilterGenresBinding
import com.gnoemes.shikimori.entity.common.domain.FilterItem
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.presentation.presenter.search.FilterGenresPresenter
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetInjectionDialogFragment
import com.gnoemes.shikimori.presentation.view.search.filter.FilterCallback
import com.gnoemes.shikimori.presentation.view.search.filter.FilterFragment
import com.gnoemes.shikimori.presentation.view.search.filter.genres.adapter.FilterGenreAdapter
import com.gnoemes.shikimori.utils.*
import com.google.gson.Gson

class FilterGenresFragment : BaseBottomSheetInjectionDialogFragment<FilterGenresPresenter, FilterGenresView>(), FilterGenresView {

    @InjectPresenter
    lateinit var filterPresenter: FilterGenresPresenter

    @ProvidePresenter
    fun providePresenter(): FilterGenresPresenter = presenterProvider.get().apply {
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
        fun newInstance(type: Type, filters: HashMap<String, MutableList<FilterItem>>?) = FilterGenresFragment()
                .withArgs {
                    putSerializable(TYPE_KEY, type)
                    putSerializable(FILTERS_KEY, filters)
                }
    }

    private val adapter by lazy { FilterGenreAdapter(presenter::onFilterInverted, presenter::onFilterSelected) }

    private var _binding: FragmentFilterGenresBinding? = null
    private val binding: FragmentFilterGenresBinding? get() = _binding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        peekHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.currentWindowMetrics.bounds.width() - context.dimenAttr(android.R.attr.actionBarSize)
        } else {
            Point().let { point ->
                @Suppress("DEPRECATION")
                activity?.windowManager?.defaultDisplay?.getSize(point)
                point
            }.x - context.dimenAttr(android.R.attr.actionBarSize)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterGenresBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = _binding ?: return

        with(b.nestedToolbar.toolbar) {
            setTitle(R.string.filters_genres)
            addBackButton(R.drawable.ic_close) { onBackPressed() }
        }

        with(b.recyclerView) {
            adapter = this@FilterGenresFragment.adapter
            layoutManager = LinearLayoutManager(context)
            itemAnimator = null
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        b.nestedToolbar.clearBtn.onClick { presenter.onResetClicked() }
        b.nestedToolbar.acceptBtn.onClick { presenter.onAcceptClicked() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun setResetEnabled(show: Boolean) { _binding?.nestedToolbar?.clearBtn?.visibleIf { show } }

    override fun onFiltersAccepted(appliedFilters: HashMap<String, MutableList<FilterItem>>) {
        val bundle = Bundle().apply {
            putString(FilterFragment.RESULT_TAG_KEY, tag)
            putString(FilterFragment.RESULT_FILTERS_KEY, Gson().toJson(appliedFilters))
        }
        parentFragmentManager.setFragmentResult(FilterFragment.GENRES_RESULT_KEY, bundle)
        onBackPressed()
    }
}