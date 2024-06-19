package com.babiichuk.waterbalancetracker.app.ui.pages.poll.ageandweight

import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AgeAndWeightViewModel @Inject constructor(
    private val userLoader: UserLoader
) : BaseViewModel() {

    val weight = MutableStateFlow<String?>(null)
    val age = MutableStateFlow<String?>(null)

    fun onNextClicked() {
        try {
            val userAge = age.value?.toInt() ?: 0
            val userWeight = weight.value?.toInt() ?: 0
            userLoader.insertUserAgeAndWeight(userAge, userWeight)
        } catch (e: Exception){
            e.stackTrace
        }
    }

}