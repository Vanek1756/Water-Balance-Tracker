package com.babiichuk.waterbalancetracker.app.ui.pages.rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.app.ui.pages.main.MainActivity
import com.babiichuk.waterbalancetracker.databinding.FragmentRateDrunkBinding
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RateDrankFragment : BaseFragment(R.layout.fragment_rate_drunk) {

    companion object {
        const val TAG = "RateDrankFragment"
    }

    private val binding by viewBinding(FragmentRateDrunkBinding::bind)
    private val viewModel: RateDrunkViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentRateDrunkBinding.setupBinding() {
        btnContinue.setOnClickListener { navigateUp() }
    }

    private fun RateDrunkViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { userFLow.collect { it?.let { bindWaterRate(it) } } }
        }
    }

    private fun bindWaterRate(user: UserEntity) {
        val recommendedRate = user.recommendedWaterRate
        val currentRate = user.currentWaterRate
        binding.apply {
            waterProgress.setWaterRate(recommendedRate)
            waterProgress.setCurrentRate(currentRate)
        }
    }

    override fun onResume() {
        (requireActivity() as MainActivity).showOrHideBottomNavigationMenu(false)
        super.onResume()
    }

    override fun onStop() {
        (requireActivity() as MainActivity).showOrHideBottomNavigationMenu(true)
        super.onStop()
    }


}