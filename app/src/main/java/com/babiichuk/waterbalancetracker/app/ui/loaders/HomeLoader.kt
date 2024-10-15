package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.core.entity.home.DefaultAmount
import com.babiichuk.waterbalancetracker.core.entity.home.DefaultCup
import com.babiichuk.waterbalancetracker.core.utils.State
import com.babiichuk.waterbalancetracker.core.utils.StateHolder
import com.babiichuk.waterbalancetracker.core.utils.getStateOrFalse
import com.babiichuk.waterbalancetracker.core.utils.mapToStateHolderList
import com.babiichuk.waterbalancetracker.core.utils.propertyChange
import com.babiichuk.waterbalancetracker.core.utils.values
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeLoader @Inject constructor(
    private val userLoader: UserLoader,
    private val cupLoader: CupLoader
    ) {

    private val _homeCupsMutableFLow: MutableStateFlow<List<StateHolder<DefaultCup>>> =
        MutableStateFlow(DefaultCup.createDefaultCups())
    val homeCupsFLow = _homeCupsMutableFLow.asStateFlow()

    private val _homeAmountMutableFLow: MutableStateFlow<List<StateHolder<DefaultAmount>>> =
        MutableStateFlow(DefaultAmount.createDefaultAmount())
    val homeAmountFLow = _homeAmountMutableFLow.asStateFlow()

    private val _openAmountDialogMutableFlow: MutableSharedFlow<Any> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val openAmountDialogFlow = _openAmountDialogMutableFlow.asSharedFlow()

    val userInfoStateFlow = userLoader.userInfoStateFlow

    fun onCupStateChanged(cupId: String) {
        _homeCupsMutableFLow.update { currentList ->
            currentList.map { cupHolder ->
                if (cupHolder.value.id != cupId) return@map cupHolder.propertyChange(State.SELECTED, false)

                cupHolder.propertyChange(State.SELECTED) { !cupHolder.getStateOrFalse(State.SELECTED) }
            }
        }
    }

    fun onAmountStateChanged(amountId: Int) {
        _homeAmountMutableFLow.update { currentList ->
            currentList.map { amountHolder ->
                if (amountHolder.value.id == DefaultAmount.DEFAULT_AMOUNT_ANOTHER){
                    val newAmount = amountHolder.value.copy(amount = DefaultAmount.DEFAULT_AMOUNT_ANOTHER)
                    val isSelected = if (amountHolder.value.id == amountId){ !amountHolder.getStateOrFalse(State.SELECTED) } else { false }
                    return@map StateHolder(newAmount, mutableMapOf(State.SELECTED to isSelected))
                }

                if (amountHolder.value.id != amountId) return@map amountHolder.propertyChange(State.SELECTED, false)

                return@map amountHolder.propertyChange(State.SELECTED) { !amountHolder.getStateOrFalse(State.SELECTED) }
            }
        }
        if (amountId == DefaultAmount.DEFAULT_AMOUNT_ANOTHER) {
            _openAmountDialogMutableFlow.tryEmit(true)
        }
    }

    fun onDialogAmountConfirm(amount: Int) {
        _homeAmountMutableFLow.update { currentList ->
            currentList.map { amountHolder ->
                if (amountHolder.value.id != DefaultAmount.DEFAULT_AMOUNT_ANOTHER) {
                   return@map amountHolder.propertyChange(State.SELECTED, false)
                }

                val newAmount = amountHolder.value.copy(amount = amount)
                return@map StateHolder(newAmount, mutableMapOf(State.SELECTED to true))
            }
        }
    }

    fun clearSelectForAmount(){
        _homeAmountMutableFLow.update { currentList ->
            currentList.values.mapToStateHolderList()
        }
    }

    fun onAddCupClicked() {
        val cupId = _homeCupsMutableFLow.value.firstOrNull { it.getStateOrFalse(State.SELECTED) }?.value?.id ?: return
        val amount = _homeAmountMutableFLow.value.firstOrNull { it.getStateOrFalse(State.SELECTED) }?.value?.amount ?: return

        cupLoader.insertCup(cupId, amount)
    }

}