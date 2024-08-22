package com.babiichuk.waterbalancetracker.app.ui.pages.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.view.tabbar.StatsBarItem
import com.babiichuk.waterbalancetracker.databinding.FragmentStatsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : BaseFragment(R.layout.fragment_stats) {

    companion object {
        const val TAG = "StatsFragment"
    }

    private val binding by viewBinding(FragmentStatsBinding::bind)
    private val viewModel: StatsViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentStatsBinding.setupBinding() {
        statsTabBar.onTabBarClickListener = { onStatsTabBarClicked(it)}
    }

    private fun onStatsTabBarClicked(barItem: StatsBarItem) {
        viewModel.loadStatisticByPeriod(barItem)
    }


    private fun StatsViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
        }
    }
}