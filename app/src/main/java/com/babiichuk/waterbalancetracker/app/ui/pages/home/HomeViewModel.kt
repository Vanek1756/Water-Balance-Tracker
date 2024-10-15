package com.babiichuk.waterbalancetracker.app.ui.pages.home

import androidx.lifecycle.viewModelScope
import com.babiichuk.waterbalancetracker.app.ui.loaders.HomeLoader
import com.babiichuk.waterbalancetracker.app.ui.pages.BaseViewModel
import com.babiichuk.waterbalancetracker.core.entity.home.DefaultAmount
import com.babiichuk.waterbalancetracker.core.entity.home.DefaultCup
import com.babiichuk.waterbalancetracker.core.entity.home.HomeContainer
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.childStateChange
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.core.utils.mapToStateHolderList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeLoader: HomeLoader
) : BaseViewModel() {

    private val _homeContentMutableFlow: MutableStateFlow<List<StateHolder<HomeContainer>>> =
        MutableStateFlow(emptyList())
    val homeContentFlow = _homeContentMutableFlow.asStateFlow()

    private val _isAddButtonVisibleMutableFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isAddButtonVisibleFlow = _isAddButtonVisibleMutableFlow.asStateFlow()

    val userFLow = homeLoader.userInfoStateFlow
    val openAmountDialogFlow = homeLoader.openAmountDialogFlow

    init {
        viewModelScope.launch {
            combine(homeLoader.homeCupsFLow, homeLoader.homeAmountFLow) { cups, amounts ->
                collectCupsAndAmount(cups, amounts)
            }.collect()
        }
    }

    private fun collectCupsAndAmount(
        listOfCups: List<StateHolder<DefaultCup>>,
        listOfAmounts: List<StateHolder<DefaultAmount>>
    ) {
        if (_homeContentMutableFlow.value.isEmpty()) {
            createHomeContainers(listOfCups, listOfAmounts)
        } else {
            updateContainers(listOfCups, listOfAmounts)
        }
        val isCupSelected = listOfCups.firstOrNull{it.getStateOrFalse(State.SELECTED)}
        val isAmountSelected = listOfAmounts.firstOrNull{it.getStateOrFalse(State.SELECTED)}

        _isAddButtonVisibleMutableFlow.update { isCupSelected != null && isAmountSelected != null }
    }

    private fun createHomeContainers(
        listOfCups: List<StateHolder<DefaultCup>>,
        listOfAmounts: List<StateHolder<DefaultAmount>>
    ) {
        val listOfContent = mutableListOf<HomeContainer>()
        listOfContent.add(HomeContainer.createDefaultCups(listOfCups))
        listOfContent.add(HomeContainer.createDefaultAmount(listOfAmounts))

        _homeContentMutableFlow.update { listOfContent.mapToStateHolderList() }
    }

    private fun updateContainers(
        listOfCups: List<StateHolder<DefaultCup>>,
        listOfAmounts: List<StateHolder<DefaultAmount>>
    ) {
        _homeContentMutableFlow.update { currentList ->
            return@update currentList.map { containerHolder ->
                val newListOfChild = when (containerHolder.value.id) {
                    HomeContainer.CUPS_CONTAINER_ID -> listOfCups
                    HomeContainer.AMOUNT_CONTAINER_ID -> listOfAmounts
                    else -> containerHolder.value.listOfChild
                }

                val newContainer = containerHolder.value.copy(listOfChild = newListOfChild)
                return@map childStateChange(newContainer, containerHolder.properties)
            }
        }
    }

    fun onCupClicked(cupId: String) {
        homeLoader.onCupStateChanged(cupId)
    }

    fun onAmountClicked(amountId: Int) {
        homeLoader.onAmountStateChanged(amountId)
    }

    fun onAddCupClicked(){
        homeLoader.onAddCupClicked()
    }

}