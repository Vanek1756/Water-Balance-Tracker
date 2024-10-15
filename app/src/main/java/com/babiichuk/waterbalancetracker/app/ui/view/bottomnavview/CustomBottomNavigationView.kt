package com.babiichuk.waterbalancetracker.app.ui.view.bottomnavview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import com.babiichuk.waterbalancetracker.core.utils.setOnSingleClickListener
import com.babiichuk.waterbalancetracker.databinding.BottomNavigationViewBinding

class CustomBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding by lazy {
        BottomNavigationViewBinding.inflate(
            LayoutInflater.from(getContext()),
            this, true
        )
    }

    private var selectedItemId: Int = 0

    var onItemClickListener: ((menuId: Int) -> Unit)? = null

    private val scaleDuration: Long = 250

    init {
        clickAction(binding.imgHome)
        selectedItemId = binding.homeFragment.id

        binding.menuSettings.setOnSingleClickListener {
            if (selectedItemId == binding.menuSettings.id) return@setOnSingleClickListener

            clickAction(binding.imgSettings)
            selectedItemId = binding.menuSettings.id
        }
        binding.homeFragment.setOnSingleClickListener {
            if (selectedItemId == binding.homeFragment.id) return@setOnSingleClickListener

            clickAction(binding.imgHome)
            onItemClickListener?.invoke(binding.homeFragment.id)
            selectedItemId = binding.homeFragment.id
        }
        binding.statsFragment.setOnSingleClickListener {
            if (selectedItemId == binding.statsFragment.id) return@setOnSingleClickListener

            clickAction(binding.imgStatistic)
            onItemClickListener?.invoke(binding.statsFragment.id)
            selectedItemId = binding.statsFragment.id
        }
    }


    private fun clickAction(imageView: ImageView) {
        defaultSize()
        imageView.animate().scaleX(1.5f).setDuration(scaleDuration).interpolator = LinearInterpolator()
        imageView.animate().scaleY(1.5f).setDuration(scaleDuration).interpolator = LinearInterpolator()
    }

    private fun defaultSize() {
        callDefault(binding.imgStatistic)
        callDefault(binding.imgHome)
        callDefault(binding.imgSettings)
    }

    private fun callDefault(imageView: ImageView) {
        imageView.animate().scaleX(1f).setDuration(scaleDuration).interpolator = LinearInterpolator()
        imageView.animate().scaleY(1f).setDuration(scaleDuration).interpolator = LinearInterpolator()
    }
}