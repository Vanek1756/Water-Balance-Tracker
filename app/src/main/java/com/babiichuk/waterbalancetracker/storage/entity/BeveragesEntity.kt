package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.babiichuk.waterbalancetracker.R

@Entity(tableName = "beverages_table")
data class BeveragesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val type: String = "",
    val nameResId: Int = 0,
    val iconId: Int,
    val volume: Int
){
    companion object Factory {
        const val DEFAULT_BEVERAGES_VOLUME: Int = 200

        fun create(userId: String, type: String, iconId: Int, volume: Int): BeveragesEntity {
            return BeveragesEntity(
                userId = userId,
                type = type,
                iconId = iconId,
                volume = volume
            )
        }

        fun create(userId: String, type: Int, iconId: Int, volume: Int): BeveragesEntity {
            return BeveragesEntity(
                userId = userId,
                nameResId = type,
                iconId = iconId,
                volume = volume
            )
        }
    }
}

enum class DefaultBeveragesType(val nameResId: Int){
    WATER(R.string.beverages_water),
    COFFEE(R.string.beverages_coffee),
    TEA(R.string.beverages_tea),
    JUICE(R.string.beverages_juice),
    WINE(R.string.beverages_wine),
    PEPSI(R.string.beverages_pepsi)
}
