package com.babiichuk.waterbalancetracker.app.ui.pages.rate

import com.babiichuk.waterbalancetracker.app.ui.loaders.HomeLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateDrunkViewModel @Inject constructor(
    private val homeLoader: HomeLoader
) : BaseViewModel() {

    val userFLow = homeLoader.userInfoStateFlow

}