package com.babiichuk.waterbalancetracker.app.ui.extensions

import com.babiichuk.waterbalancetracker.R
import com.babiichuk.waterbalancetracker.core.entity.DefaultCupEnum

fun String.getDefaultCupById() = DefaultCupEnum.entries.firstOrNull { it.id == this }

fun String.getDefaultCupIconById(): Int {
    return when (this.getDefaultCupById()) {
        DefaultCupEnum.WATER -> R.drawable.ic_cup_water
        DefaultCupEnum.COFFEE ->  R.drawable.ic_cup_coffee
        DefaultCupEnum.TEA ->  R.drawable.ic_cup_tea
        DefaultCupEnum.JUICE ->  R.drawable.ic_cup_juice
        DefaultCupEnum.MILK -> R.drawable.ic_cup_milk
        DefaultCupEnum.SODA ->  R.drawable.ic_cup_soda
        null -> R.drawable.ic_cup_water
    }
}

fun String.getDefaultCupNameById(): Int {
    return when (this.getDefaultCupById()) {
        DefaultCupEnum.WATER -> R.string.beverages_water
        DefaultCupEnum.COFFEE -> R.string.beverages_coffee
        DefaultCupEnum.TEA ->  R.string.beverages_tea
        DefaultCupEnum.JUICE ->  R.string.beverages_juice
        DefaultCupEnum.MILK -> R.string.beverages_milk
        DefaultCupEnum.SODA ->  R.string.beverages_soda
        null -> R.string.beverages_water
    }
}