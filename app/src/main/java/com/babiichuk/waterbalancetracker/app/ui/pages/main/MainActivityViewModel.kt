package com.babiichuk.waterbalancetracker.app.ui.pages.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userLoader: UserLoader,
    private val beveragesLoader: BeveragesLoader
) : ViewModel() {

    val userFlow = userLoader.userInfoStateFlow

    init {
        viewModelScope.launch {
            userLoader.authFinishedFlow.collectLatest { subscribeDataByUserId(it) }
        }
    }

    fun subscribeDataByUserId(userId: String) {
        userLoader.subscribeDataByUserId(userId)
        beveragesLoader.subscribeDataByUserId(userId)
    }

}