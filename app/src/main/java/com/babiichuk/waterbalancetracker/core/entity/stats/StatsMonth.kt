package com.babiichuk.waterbalancetracker.core.entity.stats

import com.babiichuk.waterbalancetracker.app.ui.extensions.displayText
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import java.time.LocalDate
import java.time.Month
import java.time.temporal.TemporalAdjusters

data class StatsMonth(
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate
){
    object Factory{

        fun createListOfMonth(): List<StateHolder<StatsMonth>> {
            val year = LocalDate.now().year
            val currentMonth = LocalDate.now().month
            return Month.entries.mapIndexed { index, month ->
                val isSelected = month == currentMonth
                val firstDayOfMonth = LocalDate.of(year, month, 1)
                val statsMonth = StatsMonth(
                    id = index,
                    name = month.displayText(false),
                    startDate = firstDayOfMonth,
                    endDate = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth())
                )
                return@mapIndexed StateHolder(statsMonth, mutableMapOf(State.SELECTED to isSelected))
            }
        }

    }
}