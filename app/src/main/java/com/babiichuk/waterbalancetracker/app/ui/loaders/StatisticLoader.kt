package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.propertyChange
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticLoader @Inject constructor() {

    private val _statsMonthMutableFLow: MutableStateFlow<List<StateHolder<StatsMonth>>> =
        MutableStateFlow(StatsMonth.Factory.createListOfMonth())
    val statsMonthFLow = _statsMonthMutableFLow.asStateFlow()

    fun onMonthStateChanged(monthId: Int) {
        _statsMonthMutableFLow.update { currentList ->
            currentList.map { monthHolder ->
                monthHolder.propertyChange(State.SELECTED) { monthHolder.value.id == monthId }
            }
        }
    }
}