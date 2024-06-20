package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beverages_table")
data class BeveragesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val type: String,
    val iconId: Int,
    val volume: Int
){
    companion object Factory {

        fun create(userId: String, type: String, iconId: Int, volume: Int): BeveragesEntity {
            return BeveragesEntity(
                userId = userId,
                type = type,
                iconId = iconId,
                volume = volume
            )
        }
    }
}
