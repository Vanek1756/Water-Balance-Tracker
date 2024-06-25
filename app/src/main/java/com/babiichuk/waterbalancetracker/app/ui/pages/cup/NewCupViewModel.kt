package com.babiichuk.waterbalancetracker.app.ui.pages.cup

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.core.entity.AddNewFooter
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewCupViewModel @Inject constructor(
    private val userLoader: UserLoader,
    private val beveragesLoader: BeveragesLoader
) : BaseViewModel() {

    private val _entityMutableFlow: MutableStateFlow<List<StateHolder<*>>> =
        MutableStateFlow(emptyList())
    val entityFlow = _entityMutableFlow.asStateFlow()

    val newCupName = MutableStateFlow<String?>(null)
    val newCupVolume = MutableStateFlow<String?>(null)

    private var beveragesId: Int? = null

    init {
        viewModelScope.launch {
            beveragesLoader.beveragesFlow.collectLatest {
                updateBeverages(it)
            }
        }
    }

    private fun updateBeverages(list: List<StateHolder<BeveragesEntity>>) {
        val listOfEntity = mutableListOf<StateHolder<*>>()
        listOfEntity.addAll(list)
        listOfEntity.add(createFooter())
        _entityMutableFlow.update { listOfEntity }
    }

    private fun createFooter(): StateHolder<AddNewFooter> {
        return StateHolder(AddNewFooter.create())
    }

    fun addNewBeverages() {
        if (newCupName.value == null || newCupVolume.value == null) return

        val beverageName = newCupName.value.toString()
        val volume = newCupVolume.value.toString()

        try {
            val beverageVolume = volume.toInt()
            beveragesLoader.addNewBeverage(beverageName, beverageVolume, beveragesId)
        } catch (e: Exception) {
            e.stackTrace
        }

        clearBeveragesData()
    }

    fun onBeveragesClicked(beveragesId: Int) {
        getBeveragesById(beveragesId)?.let { beverages ->
            this.beveragesId = beveragesId
            newCupName.value = beverages.nameString.ifEmpty { beverages.nameResId.toString() }
            newCupVolume.value = beverages.volume.toString()
        }
    }

    private fun getBeveragesById(beveragesId: Int): BeveragesEntity? {
        return _entityMutableFlow.value
            .map { it.value }
            .filterIsInstance<BeveragesEntity>()
            .find { it.id == beveragesId }
    }

    fun deleteBeverages() {
        beveragesId?.let { beveragesLoader.deleteBeverages(it) }
        clearBeveragesData()
    }

    private fun clearBeveragesData(){
        beveragesId = null
        newCupName.value = null
        newCupVolume.value = null
    }
}