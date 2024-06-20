package com.babiichuk.waterbalancetracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeveragesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeverage(beveragesEntity: BeveragesEntity)

    @Query("SELECT * FROM beverages_table WHERE userId = :userId")
    fun getAllBeveragesByUserIdFlow(userId: String): Flow<List<BeveragesEntity>>

    @Query("SELECT * FROM beverages_table WHERE userId = :userId")
    suspend fun getAllBeveragesByUserId(userId: String): List<BeveragesEntity>

    @Query("DELETE FROM beverages_table WHERE id = :id")
    suspend fun deleteBeverageById(id: Int)

    @Query("SELECT * FROM beverages_table WHERE id = :id")
    suspend fun getBeverageById(id: Int): BeveragesEntity?

}