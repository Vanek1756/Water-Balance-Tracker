package com.babiichuk.waterbalancetracker.app.ui.pages.stats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.statisticAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.databinding.FragmentStatsBinding
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
            launch { statsMonthFLow.collectLatest { bindMonthAdapter(it) } }
        }
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