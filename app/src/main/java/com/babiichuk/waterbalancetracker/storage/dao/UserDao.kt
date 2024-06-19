package com.babiichuk.waterbalancetracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM user_table WHERE id = :userId")
    fun getUserById(userId: String): Flow<UserEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM user_table WHERE id = :userId)")
    suspend fun isUserExists(userId: String): Boolean
}