package com.babiichuk.waterbalancetracker.storage.prefs

import com.chibatching.kotpref.KotprefModel

class ShowWaterRatePreferences(name: String) : KotprefModel() {

    override val kotprefName = name

    var isShowedScreen by booleanPref()

    var date by stringPref()

}