package com.babiichuk.waterbalancetracker.app.ui.pages.poll.calculation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.extensions.requestFocusAndShowKeyboard
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentCalculationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CalculationFragment : BaseFragment(R.layout.fragment_calculation) {

    companion object {
        const val TAG = "CalculationFragment"
    }

    private val binding by viewBinding(FragmentCalculationBinding::bind)
    private val viewModel: CalculationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentCalculationBinding.setupBinding(){
        btnBack.setOnClickListener { navigateUp() }
        tvEdit.setOnClickListener { inputRate.requestFocusAndShowKeyboard() }
        btnNext.setOnClickListener { onNextClicked() }
    }

    private fun onNextClicked() {
        viewModel.onNextClicked()

        openHomeFragment()
    }

    private fun openHomeFragment() {
        navigateTo(R.id.nav_main)
    }

    private fun CalculationViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { rateWater.bind(binding.inputRate) }
        }
    }

}