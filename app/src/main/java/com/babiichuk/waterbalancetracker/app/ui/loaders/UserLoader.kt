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

    fun getOrInsertUser(currentUser: FirebaseUser?){
        if (currentUser == null) return
        scopeIo.launch {
            val isUserExists = userRepository.isUserExists(currentUser.uid)
            if (isUserExists){
                getUserById(currentUser.uid)
            } else {
                insertUser(currentUser)
            }
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
            getUserById(userEntity.id)
        }
    }

    fun insertUserNameAndGender(name: String, gender: GenderType) {
        scopeIo.launch {
            val newData = UserProfileChangeRequest.Builder().setDisplayName(name).build()
            Firebase.auth.currentUser?.updateProfile(newData)

            _userInfoMutableStateFlow
                .updateAndGet { it?.copy (name = name, gender = gender)}
                ?.apply { userRepository.insert(this) }
        }
    }

    fun insertUserAgeAndWeight(age: Int, weight: Int) {
        scopeIo.launch {
            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(age = age, weight = weight)}
                ?.apply { userRepository.insert(this) }
        }
    }

    fun insertWaterRate(waterRate: Double) {
        scopeIo.launch {
            _userInfoMutableStateFlow
                .updateAndGet { it?.copy(waterRate = waterRate) }
                ?.let { userRepository.insert(it) }
        }
    }

    fun getUserById(userId: String) {
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