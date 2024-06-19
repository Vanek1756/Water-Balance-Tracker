package com.babiichuk.waterbalancetracker.app.ui.pages.home

import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userLoader: UserLoader
) : BaseViewModel() {
    fun signOut() {
        userLoader.signOut()
    }

}