package com.gnoemes.shikimori.presentation.view.user.holders

import android.text.Html
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.user.presentation.UserInfoViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsPlaceholderViewHolder
import com.gnoemes.shikimori.utils.*
import kotlinx.android.synthetic.main.layout_user_profile_info.view.*
import kotlinx.android.synthetic.main.layout_user_profile_info_content.view.*

class UserInfoViewHolder(
        private val view: View,
        private val actionCallback: (UserProfileAction) -> Unit
) {

    private val placeholder by lazy { DetailsPlaceholderViewHolder(view.infoContent, view.infoPlaceholder as ShimmerFrameLayout) }
    private lateinit var item: UserInfoViewModel

    init {
        with(view) {
            messageFab.background = messageFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
            messageFab.setImageDrawable(context.themeDrawable(R.drawable.ic_message, R.attr.colorOnPrimarySecondary))

            friendshipFab.background = friendshipFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
            friendshipFab.setImageDrawable(context.themeDrawable(R.drawable.ic_add_person, R.attr.colorOnPrimarySecondary))

            ignoreFab.background = ignoreFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
            ignoreFab.setImageDrawable(context.themeDrawable(R.drawable.ic_visibility_off, R.attr.colorOnPrimarySecondary))

            bansFab.background = bansFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
            bansFab.setImageDrawable(context.themeDrawable(R.drawable.ic_ban_history, R.attr.colorOnPrimarySecondary))

            messageFab.onClick { actionCallback.invoke(UserProfileAction.Message) }
            friendshipFab.onClick { actionCallback.invoke(UserProfileAction.ChangeFriendshipStatus(!item.isFriend)) }
            ignoreFab.onClick { actionCallback.invoke(UserProfileAction.ChangeIgnoreStatus(!item.isIgnored)) }
            bansFab.onClick { actionCallback.invoke(UserProfileAction.Bans) }
            aboutBtn.onClick { actionCallback.invoke(UserProfileAction.About) }
        }
    }

    fun bind(item: UserInfoViewModel) {
        this.item = item
        placeholder.showContent()

        with(view) {
            infoView.text = Html.fromHtml(item.info)

            if (item.isMe) {
                friendshipFab.gone()
                friendshipLabel.gone()
                ignoreFab.gone()
                ignoreLabel.gone()
            }

            //TODO refactor
            if (item.isFriend) {
                friendshipFab.isSelected = true
                friendshipFab.background = friendshipFab.background.apply { tint(view.context.colorAttr(R.attr.colorSecondaryTransparent)) }
                friendshipFab.setImageDrawable(context.drawable(R.drawable.ic_delete_person)?.apply { tint(colorAttr(R.attr.colorAccent)) })
            } else {
                friendshipFab.isSelected = false
                friendshipFab.background = friendshipFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
                friendshipFab.setImageDrawable(context.themeDrawable(R.drawable.ic_add_person, R.attr.colorOnPrimarySecondary))
            }

            if (item.isIgnored) {
                ignoreFab.isSelected = true
                ignoreFab.background = ignoreFab.background.apply { tint(view.context.colorAttr(R.attr.colorSecondaryTransparent)) }
                ignoreFab.setImageDrawable(context.drawable(R.drawable.ic_visibility)?.apply { tint(colorAttr(R.attr.colorAccent)) })
            } else {
                ignoreFab.isSelected = false
                ignoreFab.background = ignoreFab.background.apply { tint(view.context.colorAttr(R.attr.colorPrimary)) }
                ignoreFab.setImageDrawable(context.themeDrawable(R.drawable.ic_visibility_off, R.attr.colorOnPrimarySecondary))
            }

        }
    }

}