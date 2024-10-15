package com.babiichuk.waterbalancetracker.app.ui.pages.cup

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.BeveragesLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.CupLoader
import com.babiichuk.waterbalancetracker.app.ui.loaders.UserLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.core.entity.AddNewFooter
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.core.utils.propertyChange
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
    private val beveragesLoader: BeveragesLoader,
    private val cupsLoader: CupLoader
) : BaseViewModel() {

    private val _beveragesMutableFLow: MutableStateFlow<List<StateHolder<BeveragesEntity>>> =
        MutableStateFlow(emptyList())

    private val _entityMutableFlow: MutableStateFlow<List<StateHolder<*>>> =
        MutableStateFlow(emptyList())
    val entityFlow = _entityMutableFlow.asStateFlow()

    var intervalId: Int? = null

    init {
        viewModelScope.launch {
            beveragesLoader.beveragesFlow.collectLatest { beveragesList ->
                _beveragesMutableFLow.update { beveragesList }
                updateBeverages()
            }
        }
    }

    private fun updateBeverages() {
        val listOfEntity = mutableListOf<StateHolder<*>>()
        listOfEntity.addAll(_beveragesMutableFLow.value)
        listOfEntity.add(createFooter())
        _entityMutableFlow.update { listOfEntity }
    }

    private fun createFooter(): StateHolder<AddNewFooter> {
        return StateHolder(AddNewFooter.create())
    }

    fun onBeveragesClicked(beveragesId: Int) {
        _beveragesMutableFLow.update { currentList ->
            currentList.map { beveragesHolder ->
                if (beveragesHolder.value.id != beveragesId) return@map beveragesHolder

                beveragesHolder.propertyChange(State.SELECTED) {
                    !beveragesHolder.getStateOrFalse(State.SELECTED)
                }
            }
        }
        updateBeverages()
    }

    fun addCupsToInterval() {
//        val listOfCups = _beveragesMutableFLow.value
//            .filter { it.getStateOrFalse(State.SELECTED) }
//            .map { it.value }
//        intervalId?.let { cupsLoader.insertCup(listOfCups, it) }
    }
}