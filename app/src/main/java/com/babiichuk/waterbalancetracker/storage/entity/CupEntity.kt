package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.joda.time.DateTime

@Entity(tableName = "cup_table")
data class CupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val type: String,
    val iconId: Int,
    val volume: Int,
    val dateTime: String
){
    companion object Factory {

        fun create(beverages: BeveragesEntity): CupEntity {
            return CupEntity(
                userId = beverages.userId,
                type = beverages.type,
                iconId = beverages.iconId,
                volume = beverages.volume,
                dateTime = DateTime.now().toString()
            )
        }
    }
}