package com.babiichuk.waterbalancetracker.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.babiichuk.waterbalancetracker.app.ui.utils.GenderType

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val weight: Int,
    val gender: GenderType,
    val recommendedWaterRate: Int,
    val currentWaterRate: Int
) {

    fun isNewUser(): Boolean{
        val ageEmpty = age == 0
        val weightEmpty = weight == 0
        val genderEmpty = gender == GenderType.UNKNOWN
        val waterRateEmpty = recommendedWaterRate == 0
        return ageEmpty || weightEmpty || genderEmpty || waterRateEmpty
    }
    companion object Factory {

        fun create(name: String, gender: GenderType): UserEntity {
            return UserEntity(
                name = name,
                age = 0,
                weight = 0,
                gender = gender,
                recommendedWaterRate = 0,
                currentWaterRate = 0
            )
        }
    }
}
