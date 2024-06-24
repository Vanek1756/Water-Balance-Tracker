package com.babiichuk.waterbalancetracker.app.ui.pages.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.pages.cup.NewCupBottomSheetFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentHomeBinding
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment  : BaseFragment(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel: HomeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentHomeBinding.setupBinding(){
        btnLogUot.setOnClickListener { signOut() }
        btnAddCup.setOnClickListener { openNewCupBottomSheet() }
    }

    private fun openNewCupBottomSheet() {
        val fragment = NewCupBottomSheetFragment.newInstance()
        fragment.show(parentFragmentManager, NewCupBottomSheetFragment.TAG)
    }

    private fun signOut() {
        viewModel.signOut()
        navigateTo(R.id.nav_login)
    }

    private fun HomeViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { userFLow.collect{
                it?.let { bindWaterRate(it) } }
            }
        }
    }

    private fun bindWaterRate(user: UserEntity) {
        binding.apply {
            tvRecommendedAmount.text = getString(R.string.home_from_rate, user.recommendedWaterRate)
            tvCurrentAmount.text = getString(R.string.home_current_rate, user.currentWaterRate)
        }
    }

}