@file:Suppress("NOTHING_TO_INLINE")

package io.project.core.android.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.ViewTreeObserver.OnPreDrawListener
import android.widget.*
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.project.core.R
import io.project.core.android.adapter.AdapterViewInteractionListener
import io.project.core.app.Optional
import io.project.core.app.toOptional

inline infix fun View.setGone(gone: Boolean) {
    this.visibility = if (gone) View.GONE else View.VISIBLE
}

inline fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

inline infix fun View.setInvisible(invisible: Boolean) {
    this.visibility = if (invisible) View.INVISIBLE else View.VISIBLE
}

inline infix fun View.setWidth(@Px width: Int): Boolean {
    return setSize(width = width.toOptional())
}

inline infix fun View.setHeight(@Px height: Int): Boolean {
    return setSize(height = height.toOptional())
}

inline fun View.setSize(
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

inline fun View.margin(left: Int, top: Int, right: Int, bottom: Int) {
    val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    marginLayoutParams.setMargins(left, top, right, bottom)
    layoutParams = marginLayoutParams
}

inline fun View.heightWithMargins(): Int {
    return if (layoutParams is ViewGroup.MarginLayoutParams) {
        val marginLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
        height + marginLayoutParams.bottomMargin + marginLayoutParams.topMargin
    } else {
        height
    }
}

inline fun View.requestApplyInsetsWhenAttached() {
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

inline fun TextView.addAfterTextChangedListener(skipInitial: Boolean = false, crossinline callback: (CharSequence?) -> Unit) {
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

inline fun TextView.setOnEditorActionEventListener(actionId: Int, crossinline block: () -> Unit) {
    setOnEditorActionListener { _, _actionId, _ ->
        return@setOnEditorActionListener if (actionId == _actionId) {
            block()
            true
        } else {
            false
        }
    }
}

inline fun AutoCompleteTextView.unregisterAdapter() {
    setAdapter(null)
}

inline fun AbsSpinner.unregisterAdapter() {
    adapter = null
}

inline fun AdapterView<*>.setItemClickEventListener(noinline onItemClick: (Int) -> Unit) {
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


inline fun Spinner.setAdapterWithItemClickEvent(adapter: SpinnerAdapter, crossinline onItemClick: (Int) -> Unit) {
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

inline fun View.saveInitialPadding() {
    setTag(R.id.tag_initial_padding, recordInitialPaddingForView())
}

inline fun View.restoreInitialPadding(): InitialPadding {
    return getTag(R.id.tag_initial_padding) as InitialPadding
}

inline fun View.recordInitialPaddingForView() = InitialPadding(
    paddingLeft, paddingTop, paddingRight, paddingBottom
)


inline fun View.animateVisibility(isGone: Boolean): ViewPropertyAnimator {
    return animate()
        .alpha(if (isGone) 0f else 1f)
        .withStartAction { if (!isGone) setGone(false) }
        .withEndAction { if (isGone) setGone(true) }
}

inline fun View.setOnApplyWindowInsetsListener(crossinline listener: (WindowInsetsCompat) -> WindowInsetsCompat) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        listener(insets)
    }
}

inline fun View.onApplyAndReplaceSystemWindowInsets(
    insets: WindowInsetsCompat,
    @Px left: Int = insets.systemWindowInsetLeft,
    @Px top: Int = insets.systemWindowInsetTop,
    @Px right: Int = insets.systemWindowInsetRight,
    @Px bottom: Int = insets.systemWindowInsetBottom
): WindowInsetsCompat {
    return ViewCompat.onApplyWindowInsets(this, insets.replaceSystemWindowInsets(left, top, right, bottom))
}

inline fun WindowInsetsCompat.replaceSystemWindowInsets(
    @Px left: Int = systemWindowInsetLeft,
    @Px top: Int = systemWindowInsetTop,
    @Px right: Int = systemWindowInsetRight,
    @Px bottom: Int = systemWindowInsetBottom
): WindowInsetsCompat {
    return replaceSystemWindowInsets(left, top, right, bottom)
}
