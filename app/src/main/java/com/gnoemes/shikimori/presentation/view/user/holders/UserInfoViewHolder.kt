package com.gnoemes.shikimori.presentation.view.user.holders

import android.text.Html
import android.view.View
import com.facebook.shimmer.ShimmerFrameLayout
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.LayoutUserProfileInfoBinding
import com.gnoemes.shikimori.databinding.LayoutUserProfileInfoContentBinding
import com.gnoemes.shikimori.entity.user.presentation.UserInfoViewModel
import com.gnoemes.shikimori.entity.user.presentation.UserProfileAction
import com.gnoemes.shikimori.presentation.view.common.holders.DetailsPlaceholderViewHolder
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick

class UserInfoViewHolder(
        private val infoBinding: LayoutUserProfileInfoBinding,
        private val contentBinding: LayoutUserProfileInfoContentBinding,
        private val actionCallback: (UserProfileAction) -> Unit
) {

    private val placeholder by lazy { DetailsPlaceholderViewHolder(contentBinding.root, infoBinding.infoPlaceholder.root) }
    private lateinit var item: UserInfoViewModel

    init {
        with(contentBinding) {
            messageFab.onClick { actionCallback.invoke(if (item.isMe) UserProfileAction.MessageBox else UserProfileAction.Message) }
            friendshipFab.onClick { actionCallback.invoke(UserProfileAction.ChangeFriendshipStatus(!item.isFriend)) }
            ignoreFab.onClick { actionCallback.invoke(UserProfileAction.ChangeIgnoreStatus(!item.isIgnored)) }
            historyFab.onClick { actionCallback.invoke(UserProfileAction.History) }
            aboutBtn.onClick { actionCallback.invoke(UserProfileAction.About) }
        }
    }

    fun bind(item: UserInfoViewModel) {
        this.item = item
        placeholder.showContent()

        with(contentBinding) {
            infoView.text = Html.fromHtml(item.info)

            if (item.isMe) {
                friendshipFab.gone()
                friendshipLabel.gone()
                ignoreFab.gone()
                ignoreLabel.gone()
                messageFab.setIconResource(R.drawable.ic_mail)
                messageLabel.setText(R.string.profile_message_box)
            }

            friendshipFab.isSelected = item.isFriend
            ignoreFab.isSelected = item.isIgnored
        }
    }

    companion object {
        fun create(view: View, actionCallback: (UserProfileAction) -> Unit): UserInfoViewHolder {
            val infoBinding = LayoutUserProfileInfoBinding.bind(view)
            val contentBinding = LayoutUserProfileInfoContentBinding.bind(infoBinding.infoContent.root)
            return UserInfoViewHolder(infoBinding, contentBinding, actionCallback)
        }
    }
}