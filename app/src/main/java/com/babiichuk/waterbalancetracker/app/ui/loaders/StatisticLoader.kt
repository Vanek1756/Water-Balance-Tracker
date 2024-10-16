package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.core.utils.propertyChange
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
class StatisticLoader @Inject constructor(
    private val cupLoader: CupLoader
) {

    private val _statsMonthMutableFLow: MutableStateFlow<List<StateHolder<StatsMonth>>> =
        MutableStateFlow(StatsMonth.Factory.createListOfMonth())
    val statsMonthFLow = _statsMonthMutableFLow.asStateFlow()

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    init {
        scopeIo.launch {
            _statsMonthMutableFLow.collectLatest { updateStatsChart(it) }
        }
    }

    private fun updateStatsChart(listOfMonth: List<StateHolder<StatsMonth>>) {
        val selectedMonth = listOfMonth.find { it.getStateOrFalse(State.SELECTED) } ?: return
        cupLoader.getCupsGroupByWeekInMonth(selectedMonth.value.startDate, selectedMonth.value.endDate)
    }

    fun onMonthStateChanged(monthId: Int) {
        _statsMonthMutableFLow.update { currentList ->
            currentList.map { monthHolder ->
                monthHolder.propertyChange(State.SELECTED) { monthHolder.value.id == monthId }
            }
        }
    }
}