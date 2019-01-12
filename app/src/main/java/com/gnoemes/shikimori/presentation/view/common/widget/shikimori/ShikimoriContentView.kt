package com.gnoemes.shikimori.presentation.view.common.widget.shikimori

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.gnoemes.shikimori.R
import com.gnoemes.shikimori.entity.common.domain.Type
import com.gnoemes.shikimori.entity.common.presentation.shikimori.*
import com.gnoemes.shikimori.utils.gone
import com.gnoemes.shikimori.utils.onClick
import com.gnoemes.shikimori.utils.splitWithSavedNested
import com.gnoemes.shikimori.utils.visible
import com.gnoemes.shikimori.utils.widgets.UniqueStateLinearLayout
import kotlinx.android.synthetic.main.view_shikimori_collapsed.view.*

class ShikimoriContentView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val COLLAPSED_MAX_HEIGHT = (resources.displayMetrics.density * 80).toInt()
    private val EXPAND_BUTTON_HEIGHT = (resources.displayMetrics.density * 32).toInt()
    private var contentHeight: Int = COLLAPSED_MAX_HEIGHT
    private var isExpanded = false
    private var contentView: LinearLayout

    private var contentMargin: Int = 0
    private var contentMarginTop: Int = 0
    private var contentMarginBottom: Int = 0
    private var contentMarginLeft: Int = 0
    private var contentMarginRight: Int = 0

    var linkCallback: ((Type, Long) -> Unit)? = null
    var expandable: Boolean = true

    init {
        View.inflate(context, R.layout.view_shikimori_collapsed, this)
        contentView = createContentView()
        attrs?.let { resolveAttrs(it) }
    }

    private fun resolveAttrs(it: AttributeSet) {
        val ta = context.obtainStyledAttributes(it, R.styleable.ShikimoriContentView)

        val toolsText = ta.getText(R.styleable.ShikimoriContentView_shikimori_content)

        contentView.addView(TextView(context).apply { text = toolsText })

        expandable = ta.getBoolean(R.styleable.ShikimoriContentView_expandable, true)
        contentMargin = ta.getDimension(R.styleable.ShikimoriContentView_content_margin, 0f).toInt()
        contentMarginTop = ta.getDimension(R.styleable.ShikimoriContentView_content_margin_top, 0f).toInt()
        contentMarginLeft = ta.getDimension(R.styleable.ShikimoriContentView_content_margin_left, 0f).toInt()
        contentMarginRight = ta.getDimension(R.styleable.ShikimoriContentView_content_margin_right, 0f).toInt()
        contentMarginBottom = ta.getDimension(R.styleable.ShikimoriContentView_content_margin_bottom, 0f).toInt()

        (contentView.layoutParams as ConstraintLayout.LayoutParams).apply {
            if (contentMargin != 0) setMargins(contentMargin, contentMargin, contentMargin, contentMargin)
            else setMargins(contentMarginLeft, contentMarginTop, contentMarginRight, contentMarginBottom)
        }

        if (!expandable) expandView.gone()

        ta.recycle()
    }

    private fun createContentView(): LinearLayout {
        val view = UniqueStateLinearLayout(context)
        view.orientation = UniqueStateLinearLayout.VERTICAL
        view.id = ViewCompat.generateViewId()
        val params = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
                .apply {
                    if (contentMargin != 0) setMargins(contentMargin, contentMargin, contentMargin, contentMargin)
                    else setMargins(contentMarginLeft, contentMarginTop, contentMarginRight, contentMarginBottom)
                }
        container.addView(view, 0, params)
        val set = ConstraintSet()
        set.clone(container)
        set.connect(view.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.connect(view.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(view.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.applyTo(container)
        return view
    }

    private fun clearState() {
        container.removeView(contentView)
        contentView.removeAllViews()
        contentView = createContentView()
        contentHeight = COLLAPSED_MAX_HEIGHT
        isExpanded = false
    }

    fun setContent(content: String?) {
        clearState()
        if (content.isNullOrBlank()) return

        val items = content
                .replace(ShikimoriViews.DELIMITER, "")
                .splitWithSavedNested(ShikimoriViews.START_SYMBOL.first(), ShikimoriViews.END_SYMBOL.first())
                .map { ShikimoriViews.deserializeContent(it) }

        setContentItems(items)

        when {
            expandable -> initToggle()
            else -> expandView.gone()
        }
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
        contentView.addView(view, LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
        view.processTextContent(group)
        view.linkCallback = linkCallback
    }

    private fun initToggle() {
        contentView.post {
            contentHeight = contentView.height
            if (contentView.height >= COLLAPSED_MAX_HEIGHT) {
                contentView.layoutParams.height = COLLAPSED_MAX_HEIGHT
                contentView.requestLayout()
                expandView.visible()
                expandView.onClick { expandOrCollapse() }
            } else expandView.gone()
        }
    }

    private fun expandOrCollapse() {
        if (contentView.height >= COLLAPSED_MAX_HEIGHT) {
            isExpanded = !isExpanded
            if (isExpanded) expandView.setImageResource(R.drawable.ic_chevron_up)
            else expandView.setImageResource(R.drawable.ic_chevron_down)

            cycleHeightExpansion(contentView)
        }
    }

    private fun cycleHeightExpansion(layout: LinearLayout) {
        val end = if (layout.height == COLLAPSED_MAX_HEIGHT) contentHeight + EXPAND_BUTTON_HEIGHT else COLLAPSED_MAX_HEIGHT

        ValueAnimator.ofInt(layout.height, end)
                .apply { addUpdateListener { layout.layoutParams.apply { height = it.animatedValue as Int; layout.requestLayout() } } }
                .setDuration(500)
                .start()
    }

}