package com.gnoemes.shikimori.presentation.view.common.widget.preferences

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.StyleRes
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.gnoemes.shikimori.R
import kotlinx.android.synthetic.main.view_ascent_preference.view.*

class AscentPreference @JvmOverloads constructor(context: Context,
                                                 attrs: AttributeSet? = null,
                                                 defStyleInt: Int = 0
) : Preference(context, attrs, defStyleInt) {

    private var ascentStyle: Int = R.style.AscentStyle_Orange

    init {
        layoutResource = R.layout.view_ascent_preference
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, R.style.AscentStyle_Orange)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        if (defaultValue is Int) setAscentStyle(defaultValue)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.isClickable = false
        with(holder.itemView) {
            var id = getIdFromStyle(ascentStyle)
            chipGroup.check(id)
            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                if (checkedId == -1) group.check(id)
                else id = checkedId
                ascentStyle = getStyleFromId(checkedId)
            }
        }
    }

    fun setAscentStyle(@StyleRes ascentStyle: Int) {
        this.ascentStyle = ascentStyle

        notifyChanged()
        persistInt(ascentStyle)
    }

    fun getAscentStyle(): Int = ascentStyle

    private fun getIdFromStyle(@StyleRes style: Int): Int = when (style) {
        R.style.AscentStyle_Red -> R.id.redView
        R.style.AscentStyle_Orange -> R.id.orangeView
        R.style.AscentStyle_Yellow -> R.id.yellowView
        R.style.AscentStyle_Green -> R.id.greenView
        R.style.AscentStyle_Cyan -> R.id.cyanView
        R.style.AscentStyle_Blue -> R.id.blueView
        R.style.AscentStyle_Purple -> R.id.purpleView
        else -> 0
    }

    private fun getStyleFromId(id: Int): Int = when (id) {
        R.id.redView -> R.style.AscentStyle_Red
        R.id.orangeView -> R.style.AscentStyle_Orange
        R.id.yellowView -> R.style.AscentStyle_Yellow
        R.id.greenView -> R.style.AscentStyle_Green
        R.id.cyanView -> R.style.AscentStyle_Cyan
        R.id.blueView -> R.style.AscentStyle_Blue
        R.id.purpleView -> R.style.AscentStyle_Purple
        else -> 0
    }
}