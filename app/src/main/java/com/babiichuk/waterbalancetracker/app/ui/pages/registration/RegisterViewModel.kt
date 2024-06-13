package com.babiichuk.waterbalancetracker.app.ui.pages.registration

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.core.extensions.withProgress
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
) : BaseViewModel() {

    val accountIsCreatedFlow: MutableSharedFlow<Any> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val userEmail = MutableStateFlow<String?>(null)

    val password = MutableStateFlow<String?>(null)


    fun onRegisterClick() {
        viewModelScope.launch {
            val username = userEmail.value?.trim() ?: ""
            val password = password.value ?: ""

            mutableIsProgressVisible.withProgress {
                runCatching { createAccount(username, password) }
                    .onSuccess { }
                    .onFailure { }
            }
        }
    }

    private fun createAccount(email: String, password: String) {
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    accountIsCreatedFlow.tryEmit("success")
                } else {
                    onError(task.exception?.localizedMessage ?: "Authentication failed.")
                }
            }
    }

}