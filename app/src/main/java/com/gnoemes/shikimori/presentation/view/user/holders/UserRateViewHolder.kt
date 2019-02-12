package com.gnoemes.shikimori.presentation.view.user.holders

import android.view.Menu
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.rates.domain.RateStatus
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.entity.user.presentation.UserRateViewModel
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.visible
import kotlinx.android.synthetic.main.layout_user_profile_rates.view.*

class UserRateViewHolder(
        private val view: View,
        private val isAnime: Boolean,
        private val actionCallback: (UserProfileAction) -> Unit
) {

    private val holder: RateProgressViewHolder = RateProgressViewHolder(view.rateProgressLayout, isAnime)
    private lateinit var rawRates: Map<Int, String>
    private val menuItemClickListener =
            PopupMenu.OnMenuItemClickListener { menuItem ->
                when (menuItem?.itemId) {
                    R.id.rate_watching -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.WATCHING))
                    R.id.rate_completed -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.COMPLETED))
                    R.id.rate_dropped -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.DROPPED))
                    R.id.rate_on_hold -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.ON_HOLD))
                    R.id.rate_planned -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.PLANNED))
                    R.id.rate_rewatching -> actionCallback.invoke(UserProfileAction.RateClicked(isAnime, RateStatus.REWATCHING))
                }
                true
            }

    init {
        val rateTypeText: Int = if (isAnime) R.string.profile_rate_anime else R.string.profile_rate_manga
        view.rateLabel.setText(rateTypeText)
        view.menuView.onClick { showPopup(rawRates) }
    }

    fun bind(item: UserRateViewModel) {
        if (item.rates.toList().sumBy { it.second } == 0) {
            view.gone()
            return
        }

        this.rawRates = item.rawRates.mapKeys { mapStatusToId(it) }

        holder.bind(item.rates)

        with(view) {
            progressView.gone()
            rateProgressLayout.visible()
        }
    }

    private fun mapStatusToId(it: Map.Entry<RateStatus, String>): Int =
            when (it.key) {
                RateStatus.WATCHING -> R.id.rate_watching
                RateStatus.COMPLETED -> R.id.rate_completed
                RateStatus.DROPPED -> R.id.rate_dropped
                RateStatus.ON_HOLD -> R.id.rate_on_hold
                RateStatus.PLANNED -> R.id.rate_planned
                RateStatus.REWATCHING -> R.id.rate_rewatching
            }


    private fun showPopup(rates: Map<Int, String>) {
        val wrapper = ContextThemeWrapper(view.context, R.style.PopupMenuTheme)
        val menu = PopupMenu(wrapper, view.menuView)
                .apply {
                    setOnMenuItemClickListener(menuItemClickListener)
                    rates.entries.forEach { menu.add(Menu.NONE, it.key, Menu.NONE, it.value) }
                }
        view.post(menu::show)
    }

}