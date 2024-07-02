package com.babiichuk.waterbalancetracker.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntervalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterval(intervalEntity: IntervalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntervals(listOfInterval: List<IntervalEntity>)

    @Query("SELECT * FROM interval_table")
    fun getAllIntervalsFlow(): Flow<List<IntervalEntity>>

    @Query("SELECT * FROM interval_table")
    suspend fun getAllIntervals(): List<IntervalEntity>

    @Query("DELETE FROM interval_table WHERE id = :id")
    suspend fun deleteIntervalById(id: Int)

    @Query("SELECT * FROM interval_table WHERE id = :id")
    suspend fun getIntervalById(id: Int): IntervalEntity?
}