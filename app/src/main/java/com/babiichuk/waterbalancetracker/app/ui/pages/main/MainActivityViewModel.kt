package com.babiichuk.waterbalancetracker.app.ui.pages.main

import androidx.lifecycle.ViewModel
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userLoader: UserLoader
) : ViewModel()  {

    val userFlow = userLoader.userInfoStateFlow
}