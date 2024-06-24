package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.mapToStateHolderList
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import com.babiichuk.waterbalancetracker.storage.entity.DefaultBeveragesType
import com.babiichuk.waterbalancetracker.storage.repository.BeveragesRepository
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
class BeveragesLoader @Inject constructor(
    private val beveragesRepository: BeveragesRepository
) {

    private val _beveragesMutableFlow: MutableStateFlow<List<StateHolder<BeveragesEntity>>> =
        MutableStateFlow(emptyList())
    val beveragesFlow = _beveragesMutableFlow.asStateFlow()

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    private var userId: String = "user"

    fun subscribeDataByUserId(userId: String) {
        this.userId = userId
        scopeIo.launch {
            beveragesRepository.getBeveragesFlow(userId).collectLatest { beveragesList ->
                collectBeverages(beveragesList)
            }
        }
    }

    private fun collectBeverages(listOfEntity: List<BeveragesEntity>) {
        if (listOfEntity.isEmpty()){
            createDefaultBeverages()
            return
        }
        _beveragesMutableFlow.update { listOfEntity.mapToStateHolderList() }
    }

    private fun createDefaultBeverages() {
        val beveragesList = mutableListOf<BeveragesEntity>()
        DefaultBeveragesType.entries.forEach { type ->
            beveragesList.add(
                BeveragesEntity.create(
                    userId,
                    type.nameResId,
                    R.drawable.ic_cup,
                    BeveragesEntity.DEFAULT_BEVERAGES_VOLUME
                )
            )
        }
        addNewBeverages(beveragesList)
    }

    fun addNewBeverage(type: String, volume: Int, id: Int?) {
        scopeIo.launch {
            val newBeverages = BeveragesEntity.create(id?:0, userId, type, R.drawable.ic_cup, volume)
            beveragesRepository.insertBeverage(newBeverages)
        }
    }

    private fun addNewBeverages(beveragesList: List<BeveragesEntity>) {
        scopeIo.launch { beveragesRepository.insertBeverages(beveragesList) }
    }

    fun deleteBeverages(beveragesId: Int) {
        scopeIo.launch { beveragesRepository.deleteBeverage(beveragesId) }
    }

}