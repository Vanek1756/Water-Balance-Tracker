package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity
import com.babiichuk.waterbalancetracker.storage.repository.IntervalRepository
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
class IntervalLoader @Inject constructor(
    private val intervalRepository: IntervalRepository
) {
    private val _intervalMutableFlow: MutableStateFlow<List<IntervalEntity>> =
        MutableStateFlow(emptyList())
    val intervalFlow = _intervalMutableFlow.asStateFlow()

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    fun subscribeData() {
        scopeIo.launch {
            intervalRepository.getIntervalsFlow().collectLatest { collectInterval(it) }
        }
    }

    private fun collectInterval(entityList: List<IntervalEntity>) {
        if (entityList.isEmpty()){
            createDefaultIntervals()
            return
        }
        _intervalMutableFlow.update { entityList }
    }

    private fun createDefaultIntervals() {
        val listOfIntervals = IntervalEntity.createDefaultListOfIntervals()
        scopeIo.launch { intervalRepository.insertIntervals(listOfIntervals) }
    }

}