package com.babiichuk.waterbalancetracker.app.ui.pages.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        Firebase.auth.currentUser?.apply {
            tvUserInfo.text = this.email
        }

        btnLogUot.setOnClickListener { signOut() }
    }

    private fun signOut() {
        viewModel.signOut()
        navigateTo(R.id.nav_login)
    }

    private fun HomeViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {

        }
    }

}