package com.gnoemes.shikimori.presentation.view.common.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.FragmentTitleStatisticBinding
import com.gnoemes.shikimori.databinding.LayoutUserProfileStatisticBinding
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.presentation.view.user.adapter.UserStatisticItemAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.visibleIf
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs

class StatisticDialogFragment : BaseBottomSheetDialogFragment() {

    private var binding: FragmentTitleStatisticBinding? = null
    private var scoresBinding: LayoutUserProfileStatisticBinding? = null
    private var statusesBinding: LayoutUserProfileStatisticBinding? = null

    companion object {
        fun newInstance(title: String, scores: List<UserStatisticItem>, statuses: List<UserStatisticItem>) = StatisticDialogFragment().withArgs {
            putString(TITLE_KEY, title)
            putParcelableArray(SCORES_KEY, scores.toTypedArray())
            putParcelableArray(STATUSES_KEY, statuses.toTypedArray())
        }

        private const val TITLE_KEY = "TITLE_KEY"
        private const val SCORES_KEY = "SCORES_KEY"
        private const val STATUSES_KEY = "STATUSES_KEY"
    }

    private val scoresAdapter by lazy { UserStatisticItemAdapter() }
    private val statusesAdapter by lazy { UserStatisticItemAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTitleStatisticBinding.inflate(inflater, container, false)
        val b = binding!!
        scoresBinding = LayoutUserProfileStatisticBinding.bind(b.scoresLayout.root)
        statusesBinding = LayoutUserProfileStatisticBinding.bind(b.statusesLayout.root)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.apply {
            val defaultTitle = getString(R.string.common_statistic)
            val titleText = arguments?.getString(TITLE_KEY)?.let { "$defaultTitle «$it»" }
                    ?: defaultTitle
            title = titleText
            addBackButton(R.drawable.ic_close) { dismiss() }
        }

        scoresBinding?.headerView?.setText(R.string.profile_score)
        statusesBinding?.headerView?.setText(R.string.common_rates)

        scoresBinding?.recyclerView?.apply {
            adapter = scoresAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
        }

        statusesBinding?.recyclerView?.apply {
            adapter = statusesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
        }


        @Suppress("DEPRECATION")
        val scores = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(SCORES_KEY, UserStatisticItem::class.java)?.map { it as UserStatisticItem }
                    ?: emptyList()
        } else {
            arguments?.getParcelableArray(SCORES_KEY)?.map { it as UserStatisticItem }
                    ?: emptyList()
        }
        @Suppress("DEPRECATION")
        val statuses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArray(STATUSES_KEY, UserStatisticItem::class.java)?.map { it as UserStatisticItem }
                    ?: emptyList()
        } else {
            arguments?.getParcelableArray(STATUSES_KEY)?.map { it as UserStatisticItem }
                    ?: emptyList()
        }

        scoresBinding?.subHeaderView?.text = scores.sumOf { it.count }.toString()
        statusesBinding?.subHeaderView?.text = statuses.sumOf { it.count }.toString()

        postViewAction { scoresAdapter.bindItems(scores) }
        postViewAction { statusesAdapter.bindItems(statuses) }

        scoresBinding?.root?.visibleIf { scores.isNotEmpty() }
        statusesBinding?.root?.visibleIf { statuses.isNotEmpty() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        scoresBinding = null
        statusesBinding = null
    }

    override fun getDialogLayout(): Int = R.layout.fragment_title_statistic
}