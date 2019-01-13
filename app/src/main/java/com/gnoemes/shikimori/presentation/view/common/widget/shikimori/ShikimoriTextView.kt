package com.gnoemes.shikimori.presentation.view.common.widget.shikimori

import android.content.Context
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.widget.TextView
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.shikimori.*
import com.gnoemes.shikimori.utils.toLink
import com.gnoemes.shikimori.utils.widgets.ShikimoriLinkMovementMethod

//TODO handle url links
class ShikimoriTextView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    init {
        setTextIsSelectable(true)
        movementMethod = ShikimoriLinkMovementMethod.getInstance(object : LinkClickListener {
            override fun onLinkClicked(type: Type, id: Long) {
                linkCallback?.invoke(type, id)
            }
        })
    }

    var linkCallback: ((Type, Long) -> Unit)? = null

    fun processTextContent(group: List<Content>) {
        val builder = SpannableStringBuilder()

        group.forEach {
            when (it) {
                is Text -> appendText(builder, it)
                is Link -> appendLink(builder, it)
                is Reply -> appendReply(builder, it)
            }
        }

        text = builder
    }

    private fun appendReply(builder: SpannableStringBuilder, it: Reply) {
        val stringBuilder = SpannableStringBuilder()
        val delimiter = ", "

        it.ids.split(",")
                .forEach { id ->
                    val action = it.type.name.plus(ShikimoriViews.ACTION_DIVIDER).plus(id)
                    stringBuilder.append(id.toLink(action)).append(delimiter)
                }

        stringBuilder.replace(stringBuilder.lastIndexOf(delimiter), stringBuilder.length, "")

        val reply = context.getString(R.string.comment_reply)
        builder.append(reply)
                .append(" ")
                .append(stringBuilder)
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