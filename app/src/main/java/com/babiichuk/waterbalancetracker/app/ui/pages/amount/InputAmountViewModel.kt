package com.babiichuk.waterbalancetracker.app.ui.pages.amount

import android.text.Editable
import com.babiichuk.waterbalancetracker.app.ui.loaders.HomeLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputAmountViewModel @Inject constructor(
    private val homeLoader: HomeLoader
) : BaseViewModel() {

    fun clearSelectForAmount() {
        homeLoader.clearSelectForAmount()
    }

    fun onDialogAmountConfirm(input: Editable?) {
        if (input.isNullOrEmpty()) {
            clearSelectForAmount()
            return
        }

        try {
            val amount = input.toString().trim().toInt()
            homeLoader.onDialogAmountConfirm(amount)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}