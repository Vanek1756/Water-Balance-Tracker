package com.babiichuk.waterbalancetracker.app.ui.pages.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.adapterdelegates.cupsContainerAdapterDelegate
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.pages.cup.NewCupBottomSheetFragment
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
            cupsContainerAdapterDelegate { onAddCupClicked(it) }
        )
    }

    private fun onAddCupClicked(id: Int) {
        openNewCupBottomSheet(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentHomeBinding.setupBinding() {
//        btnAddCup.setOnClickListener { openNewCupBottomSheet() }
        rvCups.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cupsAdapter
        }
    }

    private fun openNewCupBottomSheet(id: Int) {
        val fragment = NewCupBottomSheetFragment.newInstance(id)
        fragment.show(parentFragmentManager, NewCupBottomSheetFragment.TAG)
    }

    private fun HomeViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { userFLow.collect { it?.let { bindWaterRate(it) } } }
            launch {
                cupsStateFLow.collectLatest {
                    cupsAdapter.items = it
                }
            }
        }
    }

    private fun bindWaterRate(user: UserEntity) {
        val recommendedRate =  user.recommendedWaterRate
        val currentRate =  user.currentWaterRate
        binding.apply {
            tvRecommendedAmount.text = getString(R.string.home_from_rate, recommendedRate)
            tvCurrentAmount.text = getString(R.string.home_current_rate, currentRate)
            waterProgress.max = recommendedRate
            waterProgress.setProgress(currentRate, true)
        }
    }

}