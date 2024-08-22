package com.babiichuk.waterbalancetracker.app.ui.pages.cup.dialog

import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateCupViewModel @Inject constructor(
    private val beveragesLoader: BeveragesLoader,
) : BaseViewModel() {

    val newCupName = MutableStateFlow<String?>(null)
    val newCupVolume = MutableStateFlow<String?>(null)

    private var beveragesId: Int? = null

    fun addNewBeverages() {
        if (newCupName.value == null || newCupVolume.value == null) return

        val beverageName = newCupName.value.toString()
        val volume = newCupVolume.value.toString()

        try {
            val beverageVolume = volume.toInt()
            beveragesLoader.addNewBeverage(beverageName, beverageVolume, beveragesId)
        } catch (e: Exception) {
            e.stackTrace
        }

        clearBeveragesData()
    }

    private fun clearBeveragesData() {
        beveragesId = null
        newCupName.value = null
        newCupVolume.value = null
    }

}