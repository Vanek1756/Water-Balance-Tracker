package com.babiichuk.waterbalancetracker.app.ui.pages.poll.nameandgender

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType
import com.babiichuk.waterbalancetracker.databinding.FragmentNameAndGenderBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NameAndGenderFragment : BaseFragment(R.layout.fragment_name_and_gender) {

    companion object {
        const val TAG = "NameAndGenderFragment"
    }

    private val binding by viewBinding(FragmentNameAndGenderBinding::bind)
    private val viewModel: NameAndGenderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentNameAndGenderBinding.setupBinding(){
        tvBack.setOnClickListener { navigateUp() }
        rbMan.setOnClickListener { btnNext.isEnabled = true }
        rbWoman.setOnClickListener { btnNext.isEnabled = true }
        btnNext.setOnClickListener { onNextClicked() }
    }

    private fun onNextClicked() {
        val gender = if (binding.rbMan.isChecked) GenderType.MAN else GenderType.WOMAN
        viewModel.onNextClicked(gender)

        openAgeAndWeightFragment()
    }

    private fun openAgeAndWeightFragment() {
        val action = NameAndGenderFragmentDirections.actionGenderFragmentToAgeAndWeightFragment()
        navigateTo(action)
    }

    private fun NameAndGenderViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { userName.bind(binding.inputName) }
        }
    }

}