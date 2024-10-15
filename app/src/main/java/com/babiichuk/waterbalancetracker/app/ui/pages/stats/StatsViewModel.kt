package com.babiichuk.waterbalancetracker.app.ui.pages.stats

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.StatisticLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.core.entity.stats.StatsMonth
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statisticLoader: StatisticLoader
) : BaseViewModel() {

    private val _statsMonthMutableFLow: MutableStateFlow<List<StateHolder<StatsMonth>>> =
        MutableStateFlow(emptyList())
    val statsMonthFLow = _statsMonthMutableFLow.asStateFlow()

    init {
        viewModelScope.launch {
            statisticLoader.statsMonthFLow.collectLatest { listOfMonth ->
                _statsMonthMutableFLow.update { listOfMonth }
                listOfMonth
                    .firstOrNull { it.getStateOrFalse(State.SELECTED) }
                    ?.let { loadStatisticBySelectedMonth(it) }
            }
        }
    }

    private fun loadStatisticBySelectedMonth(monthHolder: StateHolder<StatsMonth>) {

    }

    fun onMonthStateChanged(monthId: Int) {
        statisticLoader.onMonthStateChanged(monthId)
    }

}