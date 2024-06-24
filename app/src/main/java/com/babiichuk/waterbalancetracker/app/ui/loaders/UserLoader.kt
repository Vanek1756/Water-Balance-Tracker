package com.babiichuk.waterbalancetracker.app.ui.loaders

import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import com.babiichuk.waterbalancetracker.storage.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _authFinishedSharedFlow: MutableSharedFlow<String> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val authFinishedFlow = _authFinishedSharedFlow.asSharedFlow()

    fun checkAndInsertUser(currentUser: FirebaseUser?) {
        if (currentUser == null) return
        scopeIo.launch {
            val isUserExists = userRepository.isUserExists(currentUser.uid)
            if (!isUserExists) {
                insertUser(currentUser)
            } else {
                _authFinishedSharedFlow.tryEmit(currentUser.uid)
            }
//            if (isUserExists){
//                subscribeDataByUserId(currentUser.uid)
//            } else {
//                insertUser(currentUser)
//            }
        }
    }

    fun insertUser(currentUser: FirebaseUser?) {
        if (currentUser == null) return

        val userEntity = UserEntity.create(
            currentUser.uid,
            currentUser.email,
            currentUser.displayName
        )
        _userInfoMutableStateFlow.update { userEntity }
        scopeIo.launch {
            userRepository.insert(userEntity)
            _authFinishedSharedFlow.tryEmit(userEntity.id)
//            subscribeDataByUserId(userEntity.id)
        }
    }

    fun insertUserNameAndGender(name: String, gender: GenderType) {
        scopeIo.launch {
            val newData = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            Firebase.auth.currentUser?.updateProfile(newData)

            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(name = name, gender = gender) }
                ?.apply { userRepository.insert(this) }
        }
    }

    fun insertUserAgeAndWeight(age: Int, weight: Int) {
        scopeIo.launch {
            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(age = age, weight = weight) }
                ?.apply { userRepository.insert(this) }
        }
    }

    fun insertWaterRate(waterRate: Double) {
        scopeIo.launch {
            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(recommendedWaterRate = waterRate) }
                ?.let { userRepository.insert(it) }
        }
    }

    fun subscribeDataByUserId(userId: String) {
        scopeIo.launch {
            userRepository.getUserById(userId).collectLatest { userEntity ->
                _userInfoMutableStateFlow.value = userEntity
            }
        }
    }

    fun signOut() {
        _userInfoMutableStateFlow.update { null }
        scopeIo.launch { Firebase.auth.signOut() }
    }
}