package com.gnoemes.shikimori.presentation.view.common.widget.shikimori

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.shikimori.*
import com.gnoemes.shikimori.utils.splitWithSavedNested

class ShikimoriContentView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    var linkCallback: ((Type, Long) -> Unit)? = null

    init {
        orientation = HORIZONTAL
    }

    fun setContent(content: String?) {

        if (content.isNullOrBlank()) return

        val items = content
                .replace(ShikimoriViews.DELIMITER, "")
                .splitWithSavedNested(ShikimoriViews.START_SYMBOL.first(), ShikimoriViews.END_SYMBOL.first())
                .map { ShikimoriViews.deserializeContent(it) }

        setContentItems(items)
    }

    private fun setContentItems(items: List<Content>) {
        val group = mutableListOf<Content>()
        items.forEach {
            val lastItem = group.lastOrNull()
            if (lastItem == null || lastItem.contentType == it.contentType || (lastItem.contentType == ContentType.LINK || lastItem.contentType == ContentType.TEXT)) group.add(it)
            else {
                processGroup(group)
                group.clear()
            }
        }

        if (group.isNotEmpty()) processGroup(group)
    }

    private fun processGroup(group: List<Content>) {
        val firstItem = group.firstOrNull()

        when {
            firstItem is Text || firstItem is Link -> addTextGroup(group)
        }
    }

    private fun addTextGroup(group: List<Content>) {
        val view = ShikimoriTextView(context)
        addView(view)
        view.processTextContent(group)
        view.linkCallback = linkCallback
    }

}