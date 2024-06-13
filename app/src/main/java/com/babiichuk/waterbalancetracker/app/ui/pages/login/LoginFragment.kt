package com.babiichuk.waterbalancetracker.app.ui.pages.login

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
import com.babiichuk.waterbalancetracker.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        const val TAG = "LoginFragment"
    }

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setupBinding()
        viewModel.subscribe()
    }

    private fun FragmentLoginBinding.setupBinding(){
        btnSignIn.setOnClickListener { singInAccount() }
        btnSignUp.setOnClickListener { openRegisterFragment() }
    }

    private fun singInAccount() {
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

        viewModel.signInAccount(email, password)
    }

    private fun LoginViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableIsProgressVisible.collectLatest {  binding.loadingProgress.toggleVisibility(it) } }
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { signInFinishFlow.collectLatest { signInSuccess() } }
        }
    }

    private fun signInSuccess() {
        findNavController().navigate(R.id.nav_main)
    }

    private fun showError(message: String?) {
        message?.let { showToast(message, Toast.LENGTH_SHORT) }
    }

    private fun openRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}