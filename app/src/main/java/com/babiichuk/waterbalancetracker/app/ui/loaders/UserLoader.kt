package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import com.babiichuk.waterbalancetracker.storage.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserLoader @Inject constructor(
    private val userRepository: UserRepository
) {

    private val scopeIo = CoroutineScope(Dispatchers.IO)

    private val _userInfoMutableStateFlow: MutableStateFlow<UserEntity?> = MutableStateFlow(null)
    val userInfoStateFlow = _userInfoMutableStateFlow.asStateFlow()

    private val _userIsExistMutableFlow = MutableStateFlow(false)
    val userIsExistFlow = _userIsExistMutableFlow.asStateFlow()

    fun subscribeData() {
        scopeIo.launch {
            userRepository.getUserFlow().collectLatest { user ->
                _userInfoMutableStateFlow.update { user }
                _userIsExistMutableFlow.update { user != null }
            }
        }
    }

    fun insertUserNameAndGender(name: String, gender: GenderType) {
        val newUser = UserEntity.create(name, gender)
        _userInfoMutableStateFlow.update { newUser }
    }

    fun insertUserAgeAndWeight(age: Int, weight: Int) {
        _userInfoMutableStateFlow.update { it?.copy(age = age, weight = weight) }
    }

    fun insertWaterRate(waterRate: Double) {
        scopeIo.launch {
            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(recommendedWaterRate = waterRate) }
                ?.let { userRepository.insert(it) }
        }
    }

}