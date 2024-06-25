package com.babiichuk.waterbalancetracker.app.ui.pages.auth.login

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.core.extensions.withProgress
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoader: UserLoader
) : BaseViewModel() {

    val userEmail = MutableStateFlow<String?>("test1@gmail.com")

    val password = MutableStateFlow<String?>("qwerty")

    val userFlow = userLoader.userInfoStateFlow


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
//                        userLoader.checkAndInsertUser(Firebase.auth.currentUser)
                    } else {
                        onError(task.exception?.localizedMessage ?: "Authentication failed.")
                    }
                }
        }
    }

}