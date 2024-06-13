package com.babiichuk.waterbalancetracker.app.ui.pages.registration

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.extensions.showToast
import com.babiichuk.waterbalancetracker.app.ui.utils.viewBinding
import com.babiichuk.waterbalancetracker.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

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
        btnSignUp.setOnClickListener { createAccount() }
        btnSignIn.setOnClickListener { returnToLoginFragment() }
    }

    private fun RegisterViewModel.subscribe() {
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableIsProgressVisible.collectLatest {  binding.loadingProgress.toggleVisibility(it) } }
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { accountIsCreatedFlow.collectLatest { registerIsSuccess() } }
        }
    }

    private fun showError(message: String?) {
        message?.let { showToast(message, Toast.LENGTH_SHORT) }
    }

    private fun createAccount() {
        val email = binding.inputEmail.text.toString().trim()
        val password = binding.inputPassword.text.toString().trim()
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Enter email", Toast.LENGTH_LONG).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Enter password", Toast.LENGTH_LONG).show()
            return
        }

        viewModel.createAccount(email, password)
    }

    private fun registerIsSuccess() {
        findNavController().navigate(R.id.nav_main)
    }

    private fun returnToLoginFragment() {
        findNavController().navigateUp()
    }
}