package com.babiichuk.waterbalancetracker.app.ui.pages.poll.ageandweight

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentAgeAndWeightBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AgeAndWeightFragment : BaseFragment(R.layout.fragment_age_and_weight) {

    companion object {
        const val TAG = "AgeAndWeightFragment"
    }

    private val binding by viewBinding(FragmentAgeAndWeightBinding::bind)
    private val viewModel: AgeAndWeightViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentAgeAndWeightBinding.setupBinding(){
        btnBack.setOnClickListener { navigateUp() }
        btnNext.setOnClickListener { onNextClicked() }
    }

    private fun onNextClicked() {
        viewModel.onNextClicked()

        openCalculationFragment()
    }

    private fun openCalculationFragment() {
        val action = AgeAndWeightFragmentDirections.actionAgeAndWeightFragmentToCalculationFragment()
        navigateTo(action)
    }

    private fun AgeAndWeightViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { age.bind(binding.inputAge) }
            launch { weight.bind(binding.inputWeight) }
            launch {
                combine(age, weight) { age, weight ->
                    binding.btnNext.isEnabled =
                        !age?.trim().isNullOrEmpty() && !weight?.trim().isNullOrEmpty()
                }.collect()
            }
        }
    }

}