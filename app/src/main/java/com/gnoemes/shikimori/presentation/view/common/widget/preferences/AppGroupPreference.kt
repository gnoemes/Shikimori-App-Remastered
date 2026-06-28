package com.gnoemes.shikimori.presentation.view.common.widget.preferences

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceViewHolder
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.databinding.ViewDonationGroupBinding
import com.gnoemes.shikimori.utils.drawable
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick

class AppGroupPreference @JvmOverloads constructor(context: Context,
                                                   attrs: AttributeSet? = null,
                                                   defStyleInt: Int = 0
) : PreferenceGroup(context, attrs, defStyleInt) {

    init {
        layoutResource = R.layout.view_donation_group
    }

    var donationClickListener: View.OnClickListener? = null
    var mailClickListener: View.OnClickListener? = null
    var trelloClickListener: View.OnClickListener? = null
    var forumClickListener: View.OnClickListener? = null
    var clubClickListener: View.OnClickListener? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        val binding = ViewDonationGroupBinding.bind(holder.itemView)
        with(binding) {
            message.text = Html.fromHtml(context.getString(R.string.settings_donation_message))
            donationView.onClick { donationClickListener?.onClick(it) }
            btn.onClick { donationClickListener?.onClick(it) }

            sendLayout.root.icon()?.setImageDrawable(context.drawable(R.drawable.icon_mail_setting))
            sendLayout.root.title()?.text = context.getString(R.string.settings_about_send_title)
            sendLayout.root.summary()?.gone()
            sendLayout.root.onClick { mailClickListener?.onClick(it) }

            trelloLayout.root.icon()?.setImageDrawable(context.drawable(R.drawable.icon_trello_setting))
            trelloLayout.root.title()?.text = context.getString(R.string.settings_roadmap_title)
            trelloLayout.root.summary()?.gone()
            trelloLayout.root.onClick { trelloClickListener?.onClick(it) }

            fourPdaLayout.root.icon()?.setImageDrawable(context.drawable(R.drawable.icon_4pda_setting))
            fourPdaLayout.root.title()?.text = context.getString(R.string.settings_about_forum_title)
            fourPdaLayout.root.summary()?.gone()
            fourPdaLayout.root.onClick { forumClickListener?.onClick(it) }

            clubLayout.root.icon()?.setImageDrawable(context.drawable(R.drawable.icon_shikimori_setting))
            clubLayout.root.title()?.text = context.getString(R.string.settings_club_title)
            clubLayout.root.summary()?.gone()
            clubLayout.root.onClick { clubClickListener?.onClick(it) }
        }
    }

    private fun View.icon() : ImageView? = findViewById(android.R.id.icon)
    private fun View.title() : TextView? = findViewById(android.R.id.title)
    private fun View.summary() : TextView? = findViewById(android.R.id.summary)
}