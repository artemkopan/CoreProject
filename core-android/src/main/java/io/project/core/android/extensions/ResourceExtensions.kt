package io.project.core.android.extensions

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

infix fun Context.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this, colorRes)
infix fun Context.dimen(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)
infix fun Context.integer(@IntegerRes integerRes: Int) = resources.getInteger(integerRes)
infix fun Context.string(@StringRes stringRes: Int): String = resources.getString(stringRes)
fun Context.string(@StringRes stringRes: Int, args: Any): String = resources.getString(stringRes, args)
fun Context.string(@StringRes stringRes: Int, args: Array<Any>): String = resources.getString(stringRes, args)
infix fun Context.stringArray(@ArrayRes stringRes: Int): Array<out String> = resources.getStringArray(stringRes)
infix fun Context.drawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

infix fun Resources.dimen(@DimenRes dimenRes: Int) = this.getDimensionPixelSize(dimenRes)
infix fun Resources.integer(@IntegerRes integerRes: Int) = this.getInteger(integerRes)
infix fun Resources.string(@StringRes stringRes: Int): String = this.getString(stringRes)

infix fun Fragment.color(@ColorRes colorRes: Int) = ContextCompat.getColor(this.context!!, colorRes)
infix fun Fragment.dimen(@DimenRes dimenRes: Int) = resources.getDimensionPixelSize(dimenRes)
infix fun Fragment.integer(@IntegerRes integerRes: Int) = resources.getInteger(integerRes)
infix fun Fragment.string(@StringRes stringRes: Int): String = resources.getString(stringRes)
fun Fragment.string(@StringRes stringRes: Int, args: Any): String = resources.getString(stringRes, args)
infix fun Fragment.drawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(this.context!!, drawableRes)!!


fun Context.fromAttrData(
    @AttrRes attr: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    this.theme.resolveAttribute(attr, typedValue, resolveRefs)
    return typedValue.data
}


fun Context.getStatusBarHeight(@DimenRes fallbackRes: Int = View.NO_ID): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = resources dimen resourceId
    } else if (fallbackRes != View.NO_ID) {
        result = resources dimen fallbackRes
    }
    return result
}