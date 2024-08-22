package com.babiichuk.waterbalancetracker.app.ui.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat


fun View.getDrawable(@DrawableRes res: Int) = ContextCompat.getDrawable(context, res)

fun View.getColor(@ColorRes color: Int) = ContextCompat.getColor(context, color)

fun View.getString(@StringRes stringResId: Int) = context.getString(stringResId)

fun View.getString(@StringRes stringResId: Int, vararg args: Any) =
    context.getString(stringResId, *args)

@Suppress("unused")
fun View.toPx(size: Int): Int = context.toPx(size)

@Suppress("unused")
fun View.toPx(size: Float): Float = context.toPx(size)

@Suppress("unused")
fun View.spToPx(size: Int): Int = context.spToPx(size)

@Suppress("unused")
fun View.spToPx(size: Float): Float = context.spToPx(size)

fun View.hideSoftKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.setHeight(desirableHeight: Int) {
    layoutParams = layoutParams.apply {
        height = desirableHeight
    }
}

fun View.setWidth(desirableWidth: Int) {
    layoutParams = layoutParams.apply {
        width = desirableWidth
    }
}

/**
 * Show the view  (visibility = View.VISIBLE)
 */
fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Show the view if [condition] returns true
 * (visibility = View.VISIBLE)
 */
 inline fun View.showIf(condition: () -> Boolean): View {
    if (visibility != View.VISIBLE && condition()) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.hide() : View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * Remove the view if [predicate] returns true
 * (visibility = View.GONE)
 */
inline fun View.hideIf(predicate: () -> Boolean): View {
    if (visibility != View.GONE && predicate()) {
        visibility = View.GONE
    }
    return this
}

inline fun View.invisibleIf(predicate: () -> Boolean): View {
    if (visibility != View.INVISIBLE && predicate()) {
        visibility = View.INVISIBLE
    }
    return this
}

inline fun View.showOrHideIf(predicate: () -> Boolean): View {
    visibility = if (predicate()) {
        View.VISIBLE
    } else {
        View.GONE
    }
    return this
}

inline fun View.hideOrShowIf(predicate: () -> Boolean): View {
    visibility = if (predicate()) {
        View.GONE
    } else {
        View.VISIBLE
    }
    return this
}

inline fun View.showOrInvisibleIf(predicate: () -> Boolean): View {
    visibility = if (predicate()) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
    return this
}

fun View.requestFocusAndShowKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}