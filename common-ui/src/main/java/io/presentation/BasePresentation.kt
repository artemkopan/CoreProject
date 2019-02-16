package io.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import io.common.extensions.string
import io.core.common.Logger
import io.presentation.runtime.R
import java.net.ConnectException
import java.net.SocketTimeoutException


interface BasePresentation {

    fun showError(throwable: Throwable?)

    fun showError(message: String)

    fun showError(@StringRes messageRes: Int)

    fun showMessage(message: String)

    fun showMessage(@StringRes messageRes: Int)

    fun showKeyboard(view: View)

    fun hideKeyboard(view: View?)


}

interface BasePresentationLifecycleOwner {

    val lifecycleOwner: LifecycleOwner

    fun ViewState<*>.observeLoading(accept: Boolean.() -> Unit) {
        this.observeLoading(lifecycleOwner, accept)
    }

    fun <T> ViewState<T>.singleData(accept: T.() -> Unit) {
        this.singleData(lifecycleOwner, accept)
    }

    fun <T> ViewState<T>.observeData(accept: T.() -> Unit) {
        this.observeData(lifecycleOwner, accept)
    }

    fun ViewState<*>.singleError(accept: Throwable.() -> Unit) {
        this.singleError(lifecycleOwner, accept)
    }

    fun ViewState<*>.observeError(accept: Throwable.() -> Unit) {
        this.observeError(lifecycleOwner, accept)
    }
}


class BasePresentationDelegate(private val context: Context) : BasePresentation {

    override fun showError(throwable: Throwable?) {
        when (throwable) {
            is ConnectException, is SocketTimeoutException -> {
                showError(context string R.string.no_connection)
                return
            }
        }
        Logger.e(throwable)
        val message = if (throwable == null || throwable.message.isNullOrBlank()) {
            context string R.string.error_unknown
        } else {
            throwable.message!!
        }
        showError(message)
    }

    override fun showError(messageRes: Int) {
        showError(context string messageRes)
    }

    override fun showError(message: String) {
        val notificationView = findActivity()?.findViewById<View>(R.id.notificationView)
        if (notificationView is NotificationView) {
            notificationView.showError(message)
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showMessage(messageRes: Int) {
        showMessage(context string messageRes)
    }

    override fun showMessage(message: String) {
        val notificationView = findActivity()?.findViewById<View>(R.id.notificationView)
        if (notificationView is NotificationView) {
            notificationView.showMessage(message)
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun showKeyboard(view: View) {
        view.post {
            view.requestFocus()
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
                view, InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    override fun hideKeyboard(view: View?) {
        if (view == null) {
            return
        }
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun findActivity(): Activity? {
        var context = this.context
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }
}