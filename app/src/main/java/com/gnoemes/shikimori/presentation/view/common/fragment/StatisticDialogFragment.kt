package com.gnoemes.shikimori.presentation.view.common.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.UserStatisticItem
import com.gnoemes.shikimori.presentation.view.base.fragment.BaseBottomSheetDialogFragment
import com.gnoemes.shikimori.presentation.view.user.adapter.UserStatisticItemAdapter
import com.gnoemes.shikimori.utils.addBackButton
import com.gnoemes.shikimori.utils.dp
import com.gnoemes.shikimori.utils.visibleIf
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import com.gnoemes.shikimori.utils.withArgs
import kotlinx.android.synthetic.main.fragment_title_statistic.*
import kotlinx.android.synthetic.main.layout_user_profile_statistic.view.*

class StatisticDialogFragment : BaseBottomSheetDialogFragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(getDialogLayout(), container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(toolbar) {
            val defaultTitle = getString(R.string.common_statistic)
            val titleText = arguments?.getString(TITLE_KEY)?.let { "$defaultTitle «$it»" }
                    ?: defaultTitle
            title = titleText
            addBackButton(R.drawable.ic_close) { dismiss() }
        }

        scoresLayout.headerView.setText(R.string.profile_score)
        statusesLayout.headerView.setText(R.string.common_rates)

        with(scoresLayout.recyclerView) {
            adapter = scoresAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
        }

        with(statusesLayout.recyclerView) {
            adapter = statusesAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
        }


        val scores = arguments?.getParcelableArray(SCORES_KEY)?.map { it as UserStatisticItem }
                ?: emptyList()
        val statuses = arguments?.getParcelableArray(STATUSES_KEY)?.map { it as UserStatisticItem }
                ?: emptyList()

        scoresLayout.subHeaderView.text = scores.sumBy { it.count }.toString()
        statusesLayout.subHeaderView.text = statuses.sumBy { it.count }.toString()

        postViewAction { scoresAdapter.bindItems(scores) }
        postViewAction { statusesAdapter.bindItems(statuses) }

        scoresLayout.visibleIf { scores.isNotEmpty() }
        statusesLayout.visibleIf { statuses.isNotEmpty() }
    }

    ///////////////////////////////////////////////////////////////////////////
    // GETTERS
    ///////////////////////////////////////////////////////////////////////////

    override fun getDialogLayout(): Int = R.layout.fragment_title_statistic
}