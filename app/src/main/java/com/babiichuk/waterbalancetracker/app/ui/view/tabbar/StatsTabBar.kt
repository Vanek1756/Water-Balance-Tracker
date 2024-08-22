package com.babiichuk.waterbalancetracker.app.ui.view.tabbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.extensions.getColor
import com.babiichuk.waterbalancetracker.databinding.ViewStatsTabBarBinding

class StatsTabBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding by lazy {
        ViewStatsTabBarBinding.inflate(
            LayoutInflater.from(getContext()),
            this
        )
    }

    var onTabBarClickListener: ((StatsBarItem) -> Unit)? = null

    init {
        onTabClicked(StatsBarItem.DAY)

        binding.apply {
            tvDay.setOnClickListener { onTabClicked(StatsBarItem.DAY) }
            tvWeek.setOnClickListener { onTabClicked(StatsBarItem.WEEK) }
            tvMonth.setOnClickListener { onTabClicked(StatsBarItem.MONTH) }
            tvYear.setOnClickListener { onTabClicked(StatsBarItem.YEAR) }
        }
    }

    private fun onTabClicked(barItem:StatsBarItem){
        onTabBarClickListener?.invoke(barItem)

        binding.apply {
            tvDay.isSelected = barItem == StatsBarItem.DAY
            tvWeek.isSelected = barItem == StatsBarItem.WEEK
            tvMonth.isSelected = barItem == StatsBarItem.MONTH
            tvYear.isSelected = barItem == StatsBarItem.YEAR

            tvDay.setTextColor()
            tvWeek.setTextColor()
            tvMonth.setTextColor()
            tvYear.setTextColor()
        }
    }


    private fun AppCompatTextView.setTextColor(){
        val color = if (isSelected) getColor(R.color.colorWhite) else getColor(R.color.colorBlack)
        setTextColor(color)
    }

}

enum class StatsBarItem {
    DAY,
    WEEK,
    MONTH,
    YEAR
}