package com.babiichuk.waterbalancetracker.app.ui.extensions

import com.babiichuk.waterbalancetracker.app.application.locale.getAppLanguage
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT_STANDALONE else TextStyle.FULL_STANDALONE
    return getDisplayName(style, Locale(getAppLanguage())).replaceFirstChar { it.titlecase() }
}
