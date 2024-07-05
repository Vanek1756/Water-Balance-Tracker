package com.babiichuk.waterbalancetracker.app.ui.pages.deletecup

import com.babiichuk.waterbalancetracker.app.ui.loaders.CupLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeleteCupViewModel @Inject constructor(
    private val cupsLoader: CupLoader
) : BaseViewModel() {

    var cupId: Int? = null

    fun deleteCup(){
        cupId?.let { cupsLoader.deleteCup(it) }
    }

    override fun onCleared() {
        cupId = null
        super.onCleared()
    }
}