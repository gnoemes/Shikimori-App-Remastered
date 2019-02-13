package com.gnoemes.shikimori.presentation.presenter.friends.converter

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.domain.UserBrief
import com.gnoemes.shikimori.entity.user.presentation.FriendViewModel
import com.gnoemes.shikimori.utils.date.DateTimeConverter
import javax.inject.Inject

class FriendsViewModelConverterImpl @Inject constructor(
        private val dateConverter: DateTimeConverter,
        private val context: Context
) : FriendsViewModelConverter {

    private val historyNow by lazy { context.getString(R.string.history_its_now) }

    override fun apply(t: List<UserBrief>): List<FriendViewModel> =
            t.map { convertUser(it) }

    private fun convertUser(it: UserBrief): FriendViewModel {

        val rawLastOnline = context.getString(R.string.common_online) + " " + dateConverter.convertDateAgoToString(it.dateLastOnline)

        val lastOnline =
                if (rawLastOnline.contains(historyNow)) rawLastOnline.replace(historyNow, "")
                else rawLastOnline

        return FriendViewModel(
                it.id,
                it.image,
                it.nickname,
                lastOnline
        )
    }
}