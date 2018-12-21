package com.gnoemes.shikimori.presentation.view.common.widget.shikimori

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.TextView
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.shikimori.Content
import com.gnoemes.shikimori.entity.common.presentation.shikimori.Link
import com.gnoemes.shikimori.entity.common.presentation.shikimori.ShikimoriViews
import com.gnoemes.shikimori.entity.common.presentation.shikimori.Text
import com.gnoemes.shikimori.utils.toLink
import com.gnoemes.shikimori.utils.widgets.ShikimoriLinkMovementMethod

class ShikimoriTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        movementMethod = ShikimoriLinkMovementMethod.getInstance(object : LinkClickListener {
            override fun onLinkClicked(type: Type, id: Long) {
                linkCallback?.invoke(type, id)
            }
        })
    }


    var linkCallback: ((Type, Long) -> Unit)? = null

    fun setText(text: String?) {
        if (text.isNullOrBlank()) return

        val items = text
                .split(ShikimoriViews.DELIMITER)
                .map { ShikimoriViews.deserializeContent(it) }

        processTextContent(items)
    }

    private fun processTextContent(group: List<Content>) {
        val builder = SpannableStringBuilder()

        group.forEach {
            if (it is Text) {
                // html trims first whitespace
                if (it.text.startsWith(" ")) builder.append(" ")
                builder.append(Html.fromHtml(it.text))
            } else if (it is Link) {
                val action = it.type.name.plus(ShikimoriViews.ACTION_DIVIDER).plus(it.id)
                builder.append(it.text.toLink(action))
            }
        }

        text = builder
    }

    interface LinkClickListener {
        fun onLinkClicked(type: Type, id: Long)
    }

}