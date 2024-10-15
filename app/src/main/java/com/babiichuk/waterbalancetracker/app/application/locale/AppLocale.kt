package com.babiichuk.waterbalancetracker.app.application.locale

import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

fun getAppLanguage(): String {
    return AppCompatDelegate.getApplicationLocales()[0]?.language ?: Locale.getDefault().language
}