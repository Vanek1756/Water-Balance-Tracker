package com.babiichuk.waterbalancetracker.app.ui.pages.auth.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.app.ui.binding.bind
import com.babiichuk.waterbalancetracker.app.ui.binding.viewBinding
import com.babiichuk.waterbalancetracker.app.ui.extensions.launchOnLifecycle
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseFragment
import com.babiichuk.waterbalancetracker.databinding.FragmentLoginBinding
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

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
        btnSignIn.setOnClickListener { viewModel.onLoginClick() }
        btnSignUp.setOnClickListener { openRegisterFragment() }
        btnLater.setOnClickListener {  }
    }

    private fun LoginViewModel.subscribe(){
        launchOnLifecycle(Lifecycle.State.STARTED) {
            launch { mutableErrorMessage.collectLatest { showError(it) } }
            launch { userFlow.collectLatest { it?.let { signInSuccess(it) } } }
            launch { userEmail.bind(binding.inputEmail) }
            launch { password.bind(binding.inputPassword) }
        }
    }

    private fun signInSuccess(userEntity: UserEntity) {
        if (userEntity.isNewUser()) openPollsFragments() else openHomeFragment()
    }

    private fun openPollsFragments() {
        navigateTo(R.id.nav_poll)
    }
    private fun openHomeFragment() {
        navigateTo(R.id.nav_main)
    }

    private fun openRegisterFragment() {
        val action =
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        navigateTo(action)
    }
}