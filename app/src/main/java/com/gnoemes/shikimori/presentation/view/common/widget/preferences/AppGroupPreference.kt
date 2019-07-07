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
import com.gnoemes.shikimori.utils.drawable
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick
import kotlinx.android.synthetic.main.view_donation_group.view.*

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
        with(holder.itemView) {
            message.text = Html.fromHtml(context.getString(R.string.settings_donation_message))
            donationView.onClick { donationClickListener?.onClick(it) }
            btn.onClick { donationClickListener?.onClick(it) }

            sendLayout.icon()?.setImageDrawable(context.drawable(R.drawable.icon_mail_setting))
            sendLayout.title()?.text = context.getString(R.string.settings_about_send_title)
            sendLayout.summary()?.gone()
            sendLayout.onClick { mailClickListener?.onClick(it) }

            trelloLayout.icon()?.setImageDrawable(context.drawable(R.drawable.icon_trello_setting))
            trelloLayout.title()?.text = context.getString(R.string.settings_roadmap_title)
            trelloLayout.summary()?.gone()
            trelloLayout.onClick { trelloClickListener?.onClick(it) }

            fourPdaLayout.icon()?.setImageDrawable(context.drawable(R.drawable.icon_4pda_setting))
            fourPdaLayout.title()?.text = context.getString(R.string.settings_about_forum_title)
            fourPdaLayout.summary()?.gone()
            fourPdaLayout.onClick { forumClickListener?.onClick(it) }

            clubLayout.icon()?.setImageDrawable(context.drawable(R.drawable.icon_shikimori_setting))
            clubLayout.title()?.text = context.getString(R.string.settings_club_title)
            clubLayout.summary()?.gone()
            clubLayout.onClick { clubClickListener?.onClick(it) }
        }
    }

    private fun View.icon() : ImageView? = findViewById(android.R.id.icon)
    private fun View.title() : TextView? = findViewById(android.R.id.title)
    private fun View.summary() : TextView? = findViewById(android.R.id.summary)
}