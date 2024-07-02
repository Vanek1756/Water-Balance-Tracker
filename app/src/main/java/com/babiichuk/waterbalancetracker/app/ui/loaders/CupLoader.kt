package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.app.ui.utils.getTodayDate
import com.babiichuk.waterbalancetracker.core.entity.IntervalContainer
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity
import com.babiichuk.waterbalancetracker.storage.repository.CupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CupLoader @Inject constructor(
    private val cupRepository: CupRepository,
    private val intervalLoader: IntervalLoader,
    private val userLoader: UserLoader
) {

    private val _cupsMutableStateFLow: MutableStateFlow<List<StateHolder<IntervalContainer>>> =
        MutableStateFlow(emptyList())
    val cupsStateFLow = _cupsMutableStateFLow.asStateFlow()

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    fun subscribeData() {
        scopeIo.launch {
            combine(
                cupRepository.getAllCupsForTodayFlow(getTodayDate()),
                intervalLoader.intervalFlow
            ) { listOfCups, listOfInterval ->
                collectCups(listOfCups, listOfInterval)
            }.collect()
        }
    }

    private fun collectCups(listOfCups: List<CupEntity>, listOfInterval: List<IntervalEntity>) {
        val listOfContainers = listOfInterval.map { intervalEntity ->
            val intervalCups = listOfCups.filter { it.intervalId == intervalEntity.id }
            val intervalContainer = IntervalContainer.create(intervalCups, intervalEntity)
            return@map StateHolder(intervalContainer)
        }
        _cupsMutableStateFLow.update { listOfContainers }
        userLoader.updateVolume(listOfCups.sumOf { it.volume })
    }

    fun insertCups(listOfBeverages: List<BeveragesEntity>, intervalId: Int) {
        val listOfCups = listOfBeverages.map { CupEntity.create(it, intervalId) }
        scopeIo.launch { cupRepository.insertCups(listOfCups) }
    }

}