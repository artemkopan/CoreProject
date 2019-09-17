package io.project.core.android.extensions

import androidx.fragment.app.Fragment


inline fun <reified T> Fragment.findCallback(crossinline fallback: () -> T?): T? {
    return when {
        parentFragment is T -> parentFragment as T
        activity is T -> activity as T
        else -> fallback()
    }
}