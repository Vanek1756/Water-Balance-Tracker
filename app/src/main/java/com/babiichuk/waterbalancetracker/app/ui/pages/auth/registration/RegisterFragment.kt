package com.babiichuk.waterbalancetracker.app.ui.pages.auth.registration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment(R.layout.fragment_register) {

    companion object {
        const val TAG = "RegisterFragment"
    }

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentRegisterBinding.setupBinding() {
        btnSignUp.setOnClickListener { viewModel.onRegisterClick() }
        btnSignIn.setOnClickListener { returnToLoginFragment() }
    }

    private fun RegisterViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { accountIsCreatedFlow.collectLatest { registerIsSuccess() } }
            launch { userEmail.bind(binding.inputEmail) }
            launch { password.bind(binding.inputPassword) }
        }
    }

    private fun registerIsSuccess() {
        navigateTo(R.id.nav_poll)
    }

    private fun returnToLoginFragment() {
        navigateUp()
    }
}