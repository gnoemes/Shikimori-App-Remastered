package com.gnoemes.shikimori.presentation.presenter.more.provider

import android.content.Context
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.more.MoreCategory
import com.gnoemes.shikimori.entity.more.MoreCategoryItem
import com.gnoemes.shikimori.entity.more.MoreProfileItem
import com.gnoemes.shikimori.entity.user.domain.UserStatus
import javax.inject.Inject

class MoreResourceProviderImpl @Inject constructor(
        private val context: Context
) : MoreResourceProvider {


    override fun getMoreItems(): MutableList<Any> =
            mutableListOf<Any>()
                    .apply {
                        add(MoreProfileItem(UserStatus.GUEST, null, null))
                        add(MoreCategoryItem(MoreCategory.SETTINGS, R.drawable.ic_settings, R.string.more_settings))
                    }
}