package com.babiichuk.waterbalancetracker.app.ui.pages.poll

import android.os.Bundle
import android.view.View
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroFragment: BaseFragment(R.layout.fragment_intro)  {

    companion object {
        const val TAG = "IntroFragment"
    }

    private val binding by viewBinding(FragmentIntroBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
    }

    private fun FragmentIntroBinding.setupBinding(){
        btnContinue.setOnClickListener { onNextClicked() }
    }

    private fun onNextClicked() {
        val action = IntroFragmentDirections.actionIntroFragmentToGenderFragment()
        navigateTo(action)
    }
}