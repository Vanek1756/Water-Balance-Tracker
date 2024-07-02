package com.babiichuk.waterbalancetracker.storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.babiichuk.waterbalancetracker.storage.dao.BeveragesDao
import com.babiichuk.waterbalancetracker.storage.dao.IntervalDao
import com.babiichuk.waterbalancetracker.storage.dao.UserDao
import com.babiichuk.waterbalancetracker.storage.entity.BeveragesEntity
import com.babiichuk.waterbalancetracker.storage.entity.CupEntity
import com.babiichuk.waterbalancetracker.storage.entity.IntervalEntity
import com.babiichuk.waterbalancetracker.storage.entity.UserEntity

private const val DB_NAME = "app_database"

@Database(entities = [UserEntity::class, BeveragesEntity::class, CupEntity::class, IntervalEntity::class], version = 12, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun beveragesDao(): BeveragesDao
    abstract fun intervalDao(): IntervalDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}