package com.babiichuk.waterbalancetracker.app.ui.pages.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.CupLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.IntervalLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.utils.getTodayDate
import com.babiichuk.waterbalancetracker.storage.dao.WaterRatePreferencesDao
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userLoader: UserLoader,
    private val beveragesLoader: BeveragesLoader,
    private val intervalLoader: IntervalLoader,
    private val cupLoader: CupLoader,
    private val waterRatePreferencesDao: WaterRatePreferencesDao
) : ViewModel() {

    private val _showWaterRateScreenMutableFlow: MutableSharedFlow<Boolean> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val showWaterRateScreenFlow = _showWaterRateScreenMutableFlow.asSharedFlow()

    val userFlow = userLoader.userInfoStateFlow
    val userIsExistFlow = userLoader.userIsExistFlow

    init {
        subscribeData()
        viewModelScope.launch {
            userFlow.collectLatest { collectVolume(it) }
        }
    }

    private fun subscribeData() {
        userLoader.subscribeData()
        beveragesLoader.subscribeData()
        intervalLoader.subscribeData()
        cupLoader.subscribeData()
    }

    private fun collectVolume(user: UserEntity?) {
        if (user == null) {
            return
        }
        val todayDay = getTodayDate()
        val prefDate = waterRatePreferencesDao.getDate()

        if (prefDate == "") {
            viewModelScope.launch { waterRatePreferencesDao.setDate(todayDay) }
            return
        }

        if (prefDate != todayDay) {
            waterRatePreferencesDao.clear()
            viewModelScope.launch { waterRatePreferencesDao.setDate(todayDay) }
            return
        }

        val isShowedScreen = waterRatePreferencesDao.setShowedScreen()
        val isRateComplete = user.currentWaterRate >= user.recommendedWaterRate

        if (isRateComplete && !isShowedScreen) {
            viewModelScope.launch {
                waterRatePreferencesDao.setShowedScreen(true)
                _showWaterRateScreenMutableFlow.tryEmit(true)
            }
        }

        if (!isRateComplete) {
            viewModelScope.launch { waterRatePreferencesDao.setShowedScreen(false) }
        }
    }
}