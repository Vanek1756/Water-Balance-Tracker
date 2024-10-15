package com.babiichuk.waterbalancetracker.app.ui.pages.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.homeContainerAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.extensions.showOrHideIf
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.pages.amount.InputAmountDialogFragment
import com.babiichuk.waterbalancetracker.app.ui.utils.adapterdelegates.AsyncListDiffDelegationAdapter
import com.babiichuk.waterbalancetracker.databinding.FragmentHomeBinding
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    private val cupsAdapter by lazy {
        AsyncListDiffDelegationAdapter(
            homeContainerAdapterDelegate(
                onCupClicked = { viewModel.onCupClicked(it) },
                onAmountClicked = { viewModel.onAmountClicked(it) }
            )
        )
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentHomeBinding.setupBinding() {
        rvCups.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cupsAdapter
        }
        btnAdd.setOnClickListener { viewModel.onAddCupClicked() }
    }

    private fun HomeViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { userFLow.collect { it?.let { bindWaterRate(it) } } }
            launch { homeContentFlow.collectLatest { cupsAdapter.items = it } }
            launch { openAmountDialogFlow.collectLatest { startAmountDialog() } }
            launch { isAddButtonVisibleFlow.collectLatest { binding.btnAdd.showOrHideIf { it } } }
        }
    }

    private fun startAmountDialog() {
        val fragment = InputAmountDialogFragment.newInstance()
        fragment.show(parentFragmentManager, InputAmountDialogFragment.TAG)
    }

    private fun bindWaterRate(user: UserEntity) {
        val recommendedRate = user.recommendedWaterRate
        val currentRate = user.currentWaterRate
        binding.apply {
            waterProgress.setWaterRate(recommendedRate)
            waterProgress.setCurrentRate(currentRate)
        }
    }

}