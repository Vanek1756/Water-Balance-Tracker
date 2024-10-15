package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.app.ui.utils.getTodayDate
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import com.babiichuk.waterbalancetracker.storage.repository.CupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CupLoader @Inject constructor(
    private val cupRepository: CupRepository,
    private val userLoader: UserLoader
) {

    private val _currentVolumeMutableFlow:MutableStateFlow<Int> = MutableStateFlow(0)
    val currentVolumeFlow = _currentVolumeMutableFlow.asStateFlow()

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    fun subscribeData() {
        scopeIo.launch {
            cupRepository.getAllCupsForTodayFlow(getTodayDate()).collectLatest {
                collectCups(it)
            }
        }
    }

    private fun collectCups(listOfCups: List<CupEntity>) {
        _currentVolumeMutableFlow.update { listOfCups.sumOf { it.amount } }
        userLoader.updateVolume(listOfCups.sumOf { it.amount })
    }

    fun insertCup(cupId: String, amount: Int) {
        val cupEntity = CupEntity.create(cupId, amount)
        scopeIo.launch { cupRepository.insertCup(cupEntity) }
    }

    fun deleteCup(cupId: Int) {
        scopeIo.launch { cupRepository.deleteCup(cupId) }
    }

}