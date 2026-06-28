package com.infideap.drawerbehavior

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class AdvanceDrawerLayout : DrawerLayout {

    internal val settings = HashMap<Int, Setting>()

    private var defaultScrimColor = -1728053248

    private var defaultDrawerElevation = 0f

    private var frameLayout: FrameLayout? = null

    private var currentSlideOffset = 0f

    var drawerView: View? = null
        private set

    constructor(context: Context) : super(context) {
        init(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        defaultDrawerElevation = drawerElevation

        addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                this@AdvanceDrawerLayout.drawerView = drawerView
                currentSlideOffset = slideOffset
                updateSlideOffset(drawerView, slideOffset)
            }

            override fun onDrawerOpened(drawerView: View) {
                this@AdvanceDrawerLayout.drawerView = drawerView
                setScrimColor(0)
                setDrawerElevation(0f)
                frameLayout?.let { frame ->
                    for (i in 0 until frame.childCount) {
                        (frame.getChildAt(i) as? CardView)?.cardElevation = 0f
                    }
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                this@AdvanceDrawerLayout.drawerView = null
                setScrimColor(0)
            }

            override fun onDrawerStateChanged(newState: Int) {
                // no-op
            }
        })

        frameLayout = FrameLayout(context)
        super.addView(frameLayout)
    }

    override fun addView(view: View, params: ViewGroup.LayoutParams) {
        view.layoutParams = params
        addView(view)
    }

    override fun addView(view: View) {
        if (view is NavigationView) {
            super.addView(view)
        } else {
            val cardView = CardView(context)
            cardView.radius = 0f
            cardView.clipToPadding = false
            cardView.addView(view)
            cardView.cardElevation = 0f
            frameLayout?.addView(cardView)
        }
    }

    fun setViewScale(gravity: Int, percentage: Float) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        val setting = if (settings.containsKey(absGravity)) {
            settings[absGravity]!!
        } else {
            createSetting().also { settings[absGravity] = it }
        }
        setting.percentage = percentage
        setting.scrimColor = 0
        setting.drawerElevation = 0f
    }

    fun setViewElevation(gravity: Int, elevation: Float) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        val setting = if (settings.containsKey(absGravity)) {
            settings[absGravity]!!
        } else {
            createSetting().also { settings[absGravity] = it }
        }
        setting.scrimColor = 0
        setting.drawerElevation = 0f
        setting.elevation = elevation
    }

    fun setViewScrimColor(gravity: Int, scrimColor: Int) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        val setting = if (settings.containsKey(absGravity)) {
            settings[absGravity]!!
        } else {
            createSetting().also { settings[absGravity] = it }
        }
        setting.scrimColor = scrimColor
    }

    fun setDrawerElevation(gravity: Int, drawerElevation: Float) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        val setting = if (settings.containsKey(absGravity)) {
            settings[absGravity]!!
        } else {
            createSetting().also { settings[absGravity] = it }
        }
        setting.elevation = 0f
        setting.drawerElevation = drawerElevation
    }

    fun setRadius(gravity: Int, radius: Float) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        val setting = if (settings.containsKey(absGravity)) {
            settings[absGravity]!!
        } else {
            createSetting().also { settings[absGravity] = it }
        }
        setting.radius = radius
    }

    fun getSetting(gravity: Int): Setting? {
        return settings[getDrawerViewAbsoluteGravity(gravity)]
    }

    override fun setDrawerElevation(elevation: Float) {
        defaultDrawerElevation = elevation
        super.setDrawerElevation(elevation)
    }

    override fun setScrimColor(scrimColor: Int) {
        defaultScrimColor = scrimColor
        super.setScrimColor(scrimColor)
    }

    fun useCustomBehavior(gravity: Int) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        if (!settings.containsKey(absGravity)) {
            settings[absGravity] = createSetting()
        }
    }

    fun removeCustomBehavior(gravity: Int) {
        val absGravity = getDrawerViewAbsoluteGravity(gravity)
        if (settings.containsKey(absGravity)) {
            settings.remove(absGravity)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun openDrawer(view: View, animate: Boolean) {
        super.openDrawer(view, animate)
    }

    override fun closeDrawer(view: View, animate: Boolean) {
        super.closeDrawer(view, animate)
    }

    private fun updateSlideOffset(view: View, slideOffset: Float) {
        val ltrGravity = getDrawerViewAbsoluteGravity(GravityCompat.START)
        val gravity = getDrawerViewAbsoluteGravity(view)

        for (i in 0 until (frameLayout?.childCount ?: 0)) {
            val cardView = frameLayout?.getChildAt(i) as? CardView ?: continue
            val setting = settings[gravity]
            var elevation = 0f

            if (setting != null) {
                cardView.radius = (setting.radius * slideOffset).toInt().toFloat()
                super.setScrimColor(setting.scrimColor)
                super.setDrawerElevation(setting.drawerElevation)

                val percentage = 1f - setting.percentage
                val margin = height * percentage * slideOffset

                val lp = cardView.layoutParams as FrameLayout.LayoutParams
                lp.topMargin = (margin / 2f).toInt()
                lp.bottomMargin = (margin / 2f).toInt()
                cardView.layoutParams = lp

                cardView.cardElevation = setting.elevation * slideOffset
                elevation = setting.elevation

                val isSameDirection = gravity == ltrGravity
                val transX = if (isSameDirection) {
                    view.width + elevation
                } else {
                    -(view.width + elevation)
                }

                updateSlideOffset(cardView, setting, transX, slideOffset, isSameDirection)
            } else {
                super.setScrimColor(defaultScrimColor)
                super.setDrawerElevation(defaultDrawerElevation)
            }
        }
    }

    private fun updateSlideOffset(
        cardView: CardView,
        setting: Setting,
        transX: Float,
        slideOffset: Float,
        isSameDirection: Boolean
    ) {
        cardView.setX(transX * slideOffset)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerView?.let {
            updateSlideOffset(it, if (isDrawerOpen(it)) 1.0f else 0.0f)
        }
    }

    fun getDrawerViewAbsoluteGravity(gravity: Int): Int {
        return GravityCompat.getAbsoluteGravity(gravity, layoutDirection) and 0x7
    }

    fun getDrawerViewAbsoluteGravity(view: View): Int {
        val lp = view.layoutParams as DrawerLayout.LayoutParams
        return getDrawerViewAbsoluteGravity(lp.gravity)
    }

    private fun createSetting(): Setting {
        return Setting()
    }

    inner class Setting {
        var percentage: Float = 0f
        var scrimColor: Int = 0
        var elevation: Float = 0f
        var drawerElevation: Float = 0f
        var radius: Float = 0f
    }
}
