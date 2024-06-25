package com.babiichuk.waterbalancetracker.app.ui.pages.main

import androidx.lifecycle.ViewModel
import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userLoader: UserLoader,
    private val beveragesLoader: BeveragesLoader
) : ViewModel() {

    val userFlow = userLoader.userInfoStateFlow
    val userIsExistFlow = userLoader.userIsExistFlow

    fun subscribeData() {
        userLoader.subscribeData()
        beveragesLoader.subscribeData()
    }

}