package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Fade
import androidx.transition.TransitionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutUserProfileRatesBinding
import com.gnoemes.shikimori.databinding.LayoutUserProfileStatisticBinding
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel
import com.gnoemes.shikimori.presentation.view.user.adapter.UserStatisticItemAdapter
import com.gnoemes.shikimori.utils.*
import com.gnoemes.shikimori.utils.widgets.VerticalSpaceItemDecorator

class UserRateViewHolder(
        private val binding: LayoutUserProfileRatesBinding,
        private val isAnime: Boolean,
        private val actionCallback: (UserProfileAction) -> Unit,
        private val toggleCallback: (Boolean) -> Unit
) {

    private var item: UserRateViewModel? = null
    private val holder: RateProgressViewHolder = RateProgressViewHolder.create(binding.rateProgressLayout.root, isAnime)
    private val scoreAdapter by lazy { UserStatisticItemAdapter() }
    private val typesAdapter by lazy { UserStatisticItemAdapter() }
    private val ratingsAdapter by lazy { UserStatisticItemAdapter() }

    private val smallMargin by lazy { binding.root.context.dp(16) }
    private val bigMargin by lazy { binding.root.context.dp(32) }

    init {
        val rateTypeText: Int = if (isAnime) R.string.profile_rate_anime else R.string.profile_rate_manga
        binding.rateLabel.setText(rateTypeText)
        binding.menuView.onClick { actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.WATCHING)) }
        binding.scoreLayout.headerView.setText(R.string.profile_score)
        binding.typesLayout.headerView.setText(R.string.profile_types)
        binding.ratingsLayout.headerView.setText(R.string.profile_ratings)
        binding.ratingsLayout.root.visibleIf { isAnime }
        binding.arrowBtn.onClick { toggleCallback.invoke(isAnime) }

        binding.scoreLayout.headerView.apply { layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = binding.root.context.dp(16) } }
        binding.statisticLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_mini_info, 0)
        binding.statisticLabel.onClick {
            MaterialAlertDialogBuilder(binding.root.context).apply {
                setMessage(if (isAnime) R.string.profile_anime_hint else R.string.profile_manga_hint)
                setPositiveButton(R.string.common_ok, null)
            }.show()
        }

        with(binding.scoreLayout.recyclerView) {
            layoutManager = LinearLayoutManager(binding.root.context)
            addItemDecoration(VerticalSpaceItemDecorator(binding.root.context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.scoreAdapter
        }

        with(binding.typesLayout.recyclerView) {
            layoutManager = LinearLayoutManager(binding.root.context)
            addItemDecoration(VerticalSpaceItemDecorator(binding.root.context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.typesAdapter
        }

        with(binding.ratingsLayout.recyclerView) {
            layoutManager = LinearLayoutManager(binding.root.context)
            addItemDecoration(VerticalSpaceItemDecorator(binding.root.context.dp(8), true, 0, 0))
            adapter = this@UserRateViewHolder.ratingsAdapter
        }
    }

    fun toggle(expanded: Boolean) {
        binding.arrowBtn.animate().rotation(if (expanded) 180f else 0f).start()
        updateVisibility(expanded)
    }

    fun bind(item: UserRateViewModel) {
        if (item.rates.toList().sumOf { it.second } == 0) {
            binding.root.gone()
            return
        }
        this.item = item

        holder.bind(item.rates)

        scoreAdapter.bindItems(item.scores)
        typesAdapter.bindItems(item.types)
        ratingsAdapter.bindItems(item.ratings)

        with(binding) {
            progressView.root.gone()
            rateProgressLayout.root.visible()
            menuView.visible()
            val scoreSubHeader = "${root.context.getString(R.string.profile_middle_score)} ${item.averageScore}"
            scoreLayout.subHeaderView.text = scoreSubHeader
            updateVisibility(false)
        }
    }

    private fun updateVisibility(isExpanded: Boolean) {
        if (isExpanded) TransitionManager.beginDelayedTransition(binding.root as ViewGroup, Fade())
        with(binding) {
            divider.layoutParams = (divider.layoutParams as ConstraintLayout.LayoutParams).apply { topMargin = if (isExpanded) bigMargin else smallMargin }
            scoreLayout.root.visibleIf { isExpanded && item?.scores?.isNotEmpty() ?: false }
            typesLayout.root.visibleIf { isExpanded && item?.types?.isNotEmpty() ?: false }
            ratingsLayout.root.visibleIf { isExpanded && item?.ratings?.isNotEmpty() ?: false }
        }
    }

    companion object {
        fun create(view: View, isAnime: Boolean, actionCallback: (UserProfileAction) -> Unit, toggleCallback: (Boolean) -> Unit): UserRateViewHolder {
            return UserRateViewHolder(LayoutUserProfileRatesBinding.bind(view), isAnime, actionCallback, toggleCallback)
        }
    }
}