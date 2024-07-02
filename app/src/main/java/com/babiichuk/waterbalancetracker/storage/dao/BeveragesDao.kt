package com.babiichuk.waterbalancetracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BeveragesDao {

    // BeveragesEntity methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeverage(beveragesEntity: BeveragesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeverages(beveragesList: List<BeveragesEntity>)

    @Query("SELECT * FROM beverages_table")
    fun getAllBeveragesFlow(): Flow<List<BeveragesEntity>>

    @Query("SELECT * FROM beverages_table")
    suspend fun getAllBeverages(): List<BeveragesEntity>

    @Query("DELETE FROM beverages_table WHERE id = :id")
    suspend fun deleteBeverageById(id: Int)

    @Query("SELECT * FROM beverages_table WHERE id = :id")
    suspend fun getBeverageById(id: Int): BeveragesEntity?

    // CupEntity methods
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCup(cup: CupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCups(cups: List<CupEntity>)

    @Query("SELECT * FROM cup_table")
    fun getAllCupsFlow(): Flow<List<CupEntity>>

    @Query("SELECT * FROM cup_table WHERE date(dateTime) = date(:today)")
    fun getAllCupsForTodayFlow(today: String): Flow<List<CupEntity>>

    @Query("SELECT * FROM cup_table")
    suspend fun getAllCups(): List<CupEntity>

    @Query("DELETE FROM cup_table WHERE id = :id")
    suspend fun deleteCupById(id: Int)

    @Query("SELECT * FROM cup_table WHERE id = :id")
    suspend fun getCupById(id: Int): CupEntity?

}