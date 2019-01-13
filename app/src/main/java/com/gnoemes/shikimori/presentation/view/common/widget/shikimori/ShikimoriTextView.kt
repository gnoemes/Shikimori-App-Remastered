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

//TODO handle url links
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
        setTextIsSelectable(true)
    }

    var linkCallback: ((Type, Long) -> Unit)? = null

    fun processTextContent(group: List<Content>) {
        val builder = SpannableStringBuilder()

        group.forEach {
            when (it) {
                is Text -> appendText(builder, it)
                is Link -> appendLink(builder, it)
            }
        }

        text = builder
    }

    private fun appendText(builder: SpannableStringBuilder, it: Text) {
        // html trims first whitespace
        if (it.text.startsWith(" ")) builder.append(" ")
        builder.append(Html.fromHtml(it.text))
    }

    private fun appendLink(builder: SpannableStringBuilder, it: Link) {
        val action = it.type.name.plus(ShikimoriViews.ACTION_DIVIDER).plus(it.id)
        builder.append(it.text.toLink(action))
    }

    interface LinkClickListener {
        fun onLinkClicked(type: Type, id: Long)
    }

}