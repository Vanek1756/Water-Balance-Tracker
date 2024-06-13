package com.babiichuk.waterbalancetracker.app.ui.view.progress

import android.animation.AnimatorSet
import android.animation.Keyframe
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.babiichuk.waterbalancetracker.databinding.ViewProgressBinding
import com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap

open class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(wrap(context, attrs, defStyleAttr, defStyleRes), attrs, defStyleAttr, defStyleRes) {

    private val binding = ViewProgressBinding.inflate(LayoutInflater.from(getContext()), this)
    private val animation = buildAnimation()

    init {
        isClickable = true
        isFocusable = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animation.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animation.end()
    }

    protected fun showProgress() {
        binding.loaderContainer.visibility = VISIBLE
    }

    protected fun invisibleProgress() {
        binding.loaderContainer.visibility = INVISIBLE
    }

    private fun buildAnimation() = AnimatorSet().apply {
        interpolator = LinearInterpolator()
        duration = 850
        playTogether(
            ObjectAnimator.ofPropertyValuesHolder(
                binding.loader,
                PropertyValuesHolder.ofKeyframe(
                    View.ROTATION,
                    Keyframe.ofFloat(0f, 0f),
                    Keyframe.ofFloat(1f, 365.0f),
                )
            ).initInfinityRepeatCount()
        )
    }

    private fun <T: ValueAnimator> T.initInfinityRepeatCount(): T {
        repeatCount = ValueAnimator.INFINITE
        return this
    }
}