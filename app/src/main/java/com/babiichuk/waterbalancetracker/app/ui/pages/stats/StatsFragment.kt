package com.babiichuk.waterbalancetracker.app.ui.pages.stats

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.statisticAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.displayText
import com.babiichuk.waterbalancetracker.app.ui.extensions.getColor
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.extensions.toPx
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.databinding.FragmentStatsBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatsFragment : BaseFragment(R.layout.fragment_stats) {

    companion object {
        const val TAG = "StatsFragment"
    }

    private val binding by viewBinding(FragmentStatsBinding::bind)
    private val viewModel: StatsViewModel by viewModels()

    private val monthAdapter by lazy {
        AsyncListDiffDelegationAdapter(
            statisticAdapterDelegate { viewModel.onMonthStateChanged(it) }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentStatsBinding.setupBinding() {
        rvMonth.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = monthAdapter
        }
    }


    private fun StatsViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { statsMonthFLow.collectLatest { bindMonthData(it) } }
            launch { entryForChartStatsStateFlow.collectLatest { bindChart(it) } }
        }
    }

    private fun bindChart(listOfEntries: List<Entry>) {
        if (listOfEntries.isEmpty()) {
            return
        }

        listOfEntries.forEach { entry ->
            if (entry.x < 0 || entry.y < 0) {
                return
            }
        }

        val lineDataSet = LineDataSet(listOfEntries, "")
        lineDataSet.color = Color.WHITE
        lineDataSet.setCircleColor(Color.WHITE)
        lineDataSet.circleRadius = 2f
        lineDataSet.setDrawCircleHole(false)
        lineDataSet.lineWidth = 1f

        lineDataSet.valueTextColor = Color.TRANSPARENT

        lineDataSet.setDrawFilled(true)
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(Color.parseColor("#7F0B88C9"), Color.TRANSPARENT)
        )
        lineDataSet.fillDrawable = gradientDrawable
        lineDataSet.fillAlpha = 80

        val lineData = LineData(lineDataSet)

        val xAxis = binding.lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = getColor(R.color.colorGrayVariant3)
        xAxis.granularity = 1f

        val axisRight = binding.lineChart.axisRight
        axisRight.textSize = toPx(14f)
        axisRight.textColor = getColor(R.color.colorGrayVariant3)
        axisRight.granularity = 500f

        binding.lineChart.axisLeft.isEnabled = false

        binding.lineChart.animateX(1000)
        binding.lineChart.data = lineData
        binding.lineChart.legend.isEnabled = false
        binding.lineChart.description.isEnabled = false
        binding.lineChart.invalidate()
    }

    private fun bindMonthData(listOfMonth: List<StateHolder<StatsMonth>>){
        bindMonthAdapter(listOfMonth)
        bindTitles(listOfMonth)
    }

    private fun bindTitles(listOfMonth: List<StateHolder<StatsMonth>>) {
        val selectedMonth = listOfMonth.find { it.getStateOrFalse(State.SELECTED) }?.value?.startDate ?: return
        binding.tvSelectedMonth.text = selectedMonth.month.displayText(false)
        binding.tvCurrentYear.text = selectedMonth.year.toString()
    }

    private fun bindMonthAdapter(listOfMonth: List<StateHolder<StatsMonth>>) {
        val selectedPosition = listOfMonth.indexOfFirst { it.getStateOrFalse(State.SELECTED) }
        monthAdapter.setItems(listOfMonth) {
            if (selectedPosition != -1) {
                binding.rvMonth.scrollToPosition(selectedPosition)
            }
        }
    }
}