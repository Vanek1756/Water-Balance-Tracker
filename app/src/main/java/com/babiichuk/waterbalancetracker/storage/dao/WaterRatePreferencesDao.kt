package com.babiichuk.waterbalancetracker.storage.dao

import com.babiichuk.waterbalancetracker.storage.prefs.ShowWaterRatePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WaterRatePreferencesDao(
    private val showWaterRatePreferences: ShowWaterRatePreferences
) {
    fun setShowedScreen(): Boolean {
        return showWaterRatePreferences.isShowedScreen
    }

    suspend fun setShowedScreen(value: Boolean) = withContext(Dispatchers.IO) {
        showWaterRatePreferences.isShowedScreen = value
    }

    fun getDate(): String {
        return showWaterRatePreferences.date
    }

    suspend fun setDate(date:String) = withContext(Dispatchers.IO){
        showWaterRatePreferences.date = date
    }

    fun clear() {
        showWaterRatePreferences.clear()
    }
}