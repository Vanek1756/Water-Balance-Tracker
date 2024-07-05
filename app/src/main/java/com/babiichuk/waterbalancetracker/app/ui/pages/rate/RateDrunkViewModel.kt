package com.babiichuk.waterbalancetracker.app.ui.pages.rate

import com.babiichuk.waterbalancetracker.app.ui.loaders.CupLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RateDrunkViewModel @Inject constructor(
    private val cupLoader: CupLoader
) : BaseViewModel() {

    val currentVolumeFlow = cupLoader.currentVolumeFlow

}