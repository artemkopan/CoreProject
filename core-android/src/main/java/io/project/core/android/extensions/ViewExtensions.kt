package io.project.core.android.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.*
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import io.project.core.R
import io.project.core.android.adapter.AdapterViewInteractionListener
import io.project.core.app.Optional
import io.project.core.app.toOptional


infix fun View.setGone(gone: Boolean) {
    this.visibility = if (gone) View.GONE else View.VISIBLE
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

infix fun View.setInvisible(invisible: Boolean) {
    this.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

infix fun View.setWidth(@Px width: Int): Boolean {
    return setSize(width = width.toOptional())
}

infix fun View.setHeight(@Px height: Int): Boolean {
    return setSize(height = height.toOptional())
}

fun View.setSize(
    width: Optional<Int> = Optional.empty(),
    height: Optional<Int> = Optional.empty()
): Boolean {
    return layoutParams?.let {
        width.data?.run { it.width = this }
        height.data?.run { it.height = this }
        layoutParams = it
        true
    } ?: false
}

fun View.margin(left: Int, top: Int, right: Int, bottom: Int) {
    val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    marginLayoutParams.setMargins(left, top, right, bottom)
    layoutParams = marginLayoutParams
}

fun View.heightWithMargins(): Int {
    return if (layoutParams is ViewGroup.MarginLayoutParams) {
        val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
        height + marginLayoutParams.bottomMargin + marginLayoutParams.topMargin
    } else {
        height
    }
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        ViewCompat.requestApplyInsets(this)
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                ViewCompat.requestApplyInsets(this@requestApplyInsetsWhenAttached)
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

fun TextView.addAfterTextChangedListener(skipInitial: Boolean = false, callback: (CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        private var isFirstInit = true
        override fun afterTextChanged(s: Editable?) {
            if (isFirstInit && skipInitial) {
                isFirstInit = false
                return
            }
            callback(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

inline fun AutoCompleteTextView.unregisterAdapter() {
    setAdapter(null)
}

inline fun AbsSpinner.unregisterAdapter() {
    adapter = null
}

fun AdapterView<*>.setItemClickEventListener(onItemClick: (Int) -> Unit) {
    AdapterViewInteractionListener(this).onUserSelectionListener = onItemClick
}


/**
 * @param callback Return true to proceed with the current drawing pass, or false to cancel.
 */
inline fun View.callOnPreDraw(crossinline callback: (View) -> Boolean) {
    this.viewTreeObserver.addOnPreDrawListener(object : OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            if (this@callOnPreDraw.viewTreeObserver.isAlive) {
                this@callOnPreDraw.viewTreeObserver.removeOnPreDrawListener(this)
                return callback(this@callOnPreDraw)
            }
            return true
        }
    })
}


fun Spinner.setAdapterWithItemClickEvent(adapter: SpinnerAdapter, onItemClick: (Int) -> Unit) {
    this.adapter = adapter
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            onItemClick(position)
        }
    }
}


/**
 * Get relative value of X position from root View;
 * Default parentView is System DecorView;
 *
 * @return Relative X value;
 */
fun View.getRelativeX(parentView: View?): Float {
    return if (parent === parentView ?: rootView) {
        x
    } else {
        x + (parent as View).getRelativeX(parentView)
    }
}

/**
 * Get relative value of Y position from root View;
 * Default parentView is System DecorView;
 *
 * @return Relative Y value;
 */
fun View.getRelativeY(parentView: View?): Float {
    return if (parent === parentView ?: rootView) {
        y
    } else {
        y + (parent as View).getRelativeY(parentView)
    }
}


data class InitialPadding(val left: Int, val top: Int, val right: Int, val bottom: Int)

fun View.saveInitialPadding() {
    setTag(R.id.tag_initial_padding, recordInitialPaddingForView())
}

fun View.restoreInitialPadding(): InitialPadding {
    return getTag(R.id.tag_initial_padding) as InitialPadding
}

fun View.recordInitialPaddingForView() = InitialPadding(
    paddingLeft, paddingTop, paddingRight, paddingBottom
)
