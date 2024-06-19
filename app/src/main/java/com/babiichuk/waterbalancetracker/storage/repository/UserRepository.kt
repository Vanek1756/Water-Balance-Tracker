package com.babiichuk.waterbalancetracker.storage.repository

import android.util.Log
import com.babiichuk.waterbalancetracker.storage.dao.UserDao
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {
    suspend fun insert(user: UserEntity) {
        Log.e("Database", "insert: $user", )
        userDao.insert(user)
    }

    fun getUserById(userId: String): Flow<UserEntity> {
        return userDao.getUserById(userId)
    }

    suspend fun isUserExists(userId: String): Boolean {
        return userDao.isUserExists(userId)
    }
}
