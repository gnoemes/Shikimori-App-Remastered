package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.afollestad.materialdialogs.MaterialDialog
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel
import com.gnoemes.shikimori.presentation.view.user.adapter.UserStatisticItemAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator
import kotlinx.android.synthetic.main.layout_user_profile_rates.view.*
import kotlinx.android.synthetic.main.layout_user_profile_statistic.view.*

class UserRateViewHolder(
        private val view: View,
        private val isAnime: Boolean,
        private val actionCallback: (UserProfileAction) -> Unit,
        private val toggleCallback: (Boolean) -> Unit
) {

    private var item: UserRateViewModel? = null
    private val holder: RateProgressViewHolder = RateProgressViewHolder(view.rateProgressLayout, isAnime)
    private val scoreAdapter by lazy { UserStatisticItemAdapter() }
    private val typesAdapter by lazy { UserStatisticItemAdapter() }
    private val ratingsAdapter by lazy { UserStatisticItemAdapter() }

    private val smallMargin by lazy { view.context.dp(16) }
    private val bigMargin by lazy { view.context.dp(32) }

    init {
        val rateTypeText: Int = if (isAnime) R.string.profile_rate_anime else R.string.profile_rate_manga
        view.rateLabel.setText(rateTypeText)
        view.menuView.onClick { actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.WATCHING)) }
        view.scoreLayout.headerView.setText(R.string.profile_score)
        view.typesLayout.headerView.setText(R.string.profile_types)
        view.ratingsLayout.headerView.setText(R.string.profile_ratings)
        view.ratingsLayout.visibleIf { isAnime }
        view.arrowBtn.onClick { toggleCallback.invoke(isAnime) }

        view.scoreLayout.headerView.apply { layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = context.dp(16) } }
        view.statisticLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mini_info, 0)
        view.statisticLabel.onClick {
            MaterialDialog(view.context).show {
                message(res = if (isAnime) R.string.profile_anime_hint else R.string.profile_manga_hint)
                positiveButton(R.string.common_ok)
            }
        }

        with(view.scoreLayout.recyclerView) {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.scoreAdapter
        }

        with(view.typesLayout.recyclerView) {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.typesAdapter
        }

        with(view.ratingsLayout.recyclerView) {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(VerticalSpaceItemDecorator(context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.ratingsAdapter
        }
    }

    fun toggle(expanded: Boolean) {
        view.arrowBtn.animate().rotation(if (expanded) 180f else 0f).start()
        updateVisibility(expanded)
    }

    fun bind(item: UserRateViewModel) {
        if (item.rates.toList().sumBy { it.second } == 0) {
            view.gone()
            return
        }
        this.item = item

        holder.bind(item.rates)

        scoreAdapter.bindItems(item.scores)
        typesAdapter.bindItems(item.types)
        ratingsAdapter.bindItems(item.ratings)

        with(view) {
            progressView.gone()
            rateProgressLayout.visible()
            menuView.visible()
            val scoreSubHeader = "${context.getString(R.string.profile_middle_score)} ${item.averageScore}"
            scoreLayout.subHeaderView.text = scoreSubHeader
            updateVisibility(false)
        }
    }

    private fun updateVisibility(isExpanded: Boolean) {
        if (isExpanded) TransitionManager.beginDelayedTransition(view as ViewGroup, Fade())
        with(view) {
            divider.layoutParams = (divider.layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = if (isExpanded) bigMargin else smallMargin }
            scoreLayout.visibleIf { isExpanded && item?.scores?.isNotEmpty() ?: false }
            typesLayout.visibleIf { isExpanded && item?.types?.isNotEmpty() ?: false }
            ratingsLayout.visibleIf { isExpanded && item?.ratings?.isNotEmpty() ?: false }
        }
    }
}