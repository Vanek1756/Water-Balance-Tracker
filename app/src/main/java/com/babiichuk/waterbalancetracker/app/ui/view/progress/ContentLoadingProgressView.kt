package com.babiichuk.waterbalancetracker.app.ui.view.progress

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.doOnAttach
import androidx.core.view.isVisible
import com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap

class ContentLoadingProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes
    defStyleAttr: Int = 0,
    @StyleRes
    defStyleRes: Int = 0
) : ProgressView(wrap(context, attrs, defStyleAttr, defStyleRes), attrs, defStyleAttr, defStyleRes) {

    companion object {
        const val DEFAULT_MIN_SHOW_TIME = 500
        const val DEFAULT_MIN_DELAY = 500
    }

    private val minSowTime: Int
    private val minDelay: Int

    private var startTime = -1L
    private var postedHide = false
    private var postedShow = false
    private var dismissed = false

    init {
        minSowTime = DEFAULT_MIN_SHOW_TIME
        minDelay = DEFAULT_MIN_DELAY
        isVisible = false
    }

    private val delayedHide = Runnable {
        postedHide = false
        startTime = -1
        visibility = View.INVISIBLE
    }

    private val delayedShow = Runnable {
        postedShow = false
        if (!dismissed) {
            startTime = System.currentTimeMillis()
            showProgress()
            visibility = View.VISIBLE
        }
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        removeCallbacks()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks()
    }

    private fun removeCallbacks() {
        removeCallbacks(delayedHide)
        removeCallbacks(delayedShow)
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @Synchronized
    fun hide() {
        dismissed = true
        removeCallbacks(delayedShow)
        postedShow = false
        val diff = System.currentTimeMillis() - startTime
        if (diff >= minSowTime || startTime == -1L) {
            // The progress spinner has been shown long enough
            // OR was not shown yet. If it wasn't shown yet,
            // it will just never be shown.
            visibility = View.GONE
        } else {
            // The progress spinner is shown, but not long enough,
            // so put a delayed message in to hide it when its been
            // shown long enough.
            if (!postedHide) {
                postDelayed(delayedHide, minSowTime - diff)
                postedHide = true
            }
        }
    }

    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @Synchronized
    fun show() {
        // Reset the start time.
        if (dismissed) {
            showProgressViewButProgressWillBeInvisible()
        }
        startTime = -1
        dismissed = false
        removeCallbacks(delayedHide)
        postedHide = false
        if (!postedShow) {
            postDelayed(delayedShow, minDelay.toLong())
            postedShow = true
        }
    }

    @Synchronized
    fun toggleVisibility(visible: Boolean) {
        doOnAttach {
            if (visible) {
                show()
            } else {
                hide()
            }
        }
    }

    /**
     * Show the invisible progress view for blocking clicks under progress
     * u will see progress animation after DEFAULT_MIN_DELAY
     */
    @Synchronized
    private fun showProgressViewButProgressWillBeInvisible() {
        visibility = VISIBLE
        invisibleProgress()
    }
}