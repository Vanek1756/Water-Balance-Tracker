package com.babiichuk.waterbalancetracker.app.ui.pages.login

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.core.extensions.withProgress
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
) : BaseViewModel() {

    val signInFinishFlow: MutableSharedFlow<Any> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val userEmail = MutableStateFlow<String?>(null)

    val password = MutableStateFlow<String?>(null)


    fun onLoginClick() {
        viewModelScope.launch {
            val username = userEmail.value?.trim() ?: ""
            val password = password.value ?: ""

            mutableIsProgressVisible.withProgress {
                runCatching { signInAccount(username, password) }
                    .onSuccess { }
                    .onFailure { }
            }
        }
    }

    private fun signInAccount(email: String, password: String) {
        mutableIsProgressVisible.withProgress {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        signInFinishFlow.tryEmit("success")
                    } else {
                        onError(task.exception?.localizedMessage ?: "Authentication failed.")
                    }
                }
        }
    }

}