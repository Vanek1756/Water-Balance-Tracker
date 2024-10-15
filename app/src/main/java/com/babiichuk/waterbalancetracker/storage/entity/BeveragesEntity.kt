package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "beverages_table")
data class BeveragesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nameString: String = "",
    val nameResId: Int = 0,
    val iconId: Int,
    val volume: Int
){
    companion object Factory {
        const val DEFAULT_BEVERAGES_VOLUME: Int = 200

        fun create(id: Int, type: String, iconId: Int, volume: Int): BeveragesEntity {
            return BeveragesEntity(
                id = id,
                nameString = type,
                iconId = iconId,
                volume = volume
            )
        }

        fun create(type: Int, iconId: Int, volume: Int): BeveragesEntity {
            return BeveragesEntity(
                nameResId = type,
                iconId = iconId,
                volume = volume
            )
        }
    }
}
