package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.app.ui.utils.getTodayDate
import com.babiichuk.waterbalancetracker.app.ui.utils.parseToDate
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import com.babiichuk.waterbalancetracker.storage.repository.CupRepository
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CupLoader @Inject constructor(
    private val cupRepository: CupRepository,
    private val userLoader: UserLoader
) {

    private val _currentVolumeMutableFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentVolumeFlow = _currentVolumeMutableFlow.asStateFlow()

    private val _entryForChartStatsMutableStateFlow: MutableStateFlow<List<Entry>> =
        MutableStateFlow(emptyList())
    val entryForChartStatsStateFlow = _entryForChartStatsMutableStateFlow.asStateFlow()

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

    fun getCupsGroupByWeekInMonth(startDate: LocalDate, endDate:LocalDate){
        scopeIo.launch {
            val listOfCupByPeriod = cupRepository.getAllCupsByPeriod(startDate.toString(), endDate.toString())

            val listDaysOfMonth = if (startDate.month == LocalDate.now().month){
                (1..LocalDate.now().dayOfMonth).toList()
            } else {
                (1..startDate.month.maxLength()).toList()
            }

            val entries = listOfCupByPeriod
                .groupBy { parseToDate(it).dayOfMonth }
                .mapValues { entry ->
                    entry.value.sumOf { it.amount }
                }
            val resultMap = listDaysOfMonth.associateWith { entries[it] ?: 0 }
                .map { (day, amount) ->
                    Entry(day.toFloat(), amount.toFloat())
                }

            _entryForChartStatsMutableStateFlow.update { resultMap }
        }
    }

}