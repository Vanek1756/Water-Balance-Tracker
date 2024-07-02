package com.babiichuk.waterbalancetracker.app.ui.pages.poll.calculation

import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.app.ui.utils.DEFAULT_WATER_RATE
import com.babiichuk.waterbalancetracker.core.utils.calculateDailyWaterIntake
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CalculationViewModel @Inject constructor(
    private val userLoader: UserLoader
) : BaseViewModel() {

    val rateWater = MutableStateFlow(calculateDailyWaterRate())

    fun onNextClicked() {
        userLoader.insertWaterRate(rateWater.value)
    }

    private fun calculateDailyWaterRate(): Int {
        var result = DEFAULT_WATER_RATE
        userLoader.userInfoStateFlow.value?.let {
            result = calculateDailyWaterIntake(it.gender,it.age, it.weight)
        }
        return result
    }
}