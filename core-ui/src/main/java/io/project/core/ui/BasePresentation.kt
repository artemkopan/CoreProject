package io.project.core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import io.project.core.android.extensions.string
import io.project.core.app.Logger
import io.project.core.ui.widgets.NotificationView
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

    fun showLoading(isLoading: Boolean)

}

interface PresentationLifecycle {

    val presentationLifecycleOwner: LifecycleOwner

    val instanceLifecycleOwner: LifecycleOwner

    fun ViewState<*>.observeLoading(accept: Boolean.() -> Unit) {
        this.observeLoading(presentationLifecycleOwner, accept)
    }

    fun <T> ViewState<T>.singleData(accept: T.() -> Unit) {
        this.singleData(presentationLifecycleOwner, accept)
    }

    fun <T> ViewState<T>.observeData(accept: T.() -> Unit) {
        this.observeData(presentationLifecycleOwner, accept)
    }

    fun ViewState<*>.singleError(accept: Throwable.() -> Unit) {
        this.singleError(presentationLifecycleOwner, accept)
    }

    fun ViewState<*>.observeError(accept: Throwable.() -> Unit) {
        this.observeError(presentationLifecycleOwner, accept)
    }
}

fun <T, P> P.bindViewState(
    receiver: ViewState<T>,
    onNext: ((T) -> Unit)? = null,
    onError: ((Throwable) -> Unit)? = { showError(it) },
    onLoading: ((Boolean) -> Unit)? = { showLoading(it) },
    onSingle: ((T) -> Unit)? = null,
    onResult: ((T?, Throwable?) -> Unit)? = null
) where P : BasePresentation, P : PresentationLifecycle {
    onLoading?.let { receiver.observeLoading { it.invoke(this) } }
    onError?.let { receiver.singleError { it.invoke(this); onResult?.invoke(null, this) } }
    onNext?.let { receiver.observeData { it.invoke(this); onResult?.invoke(this, null) } }
    onSingle?.let { receiver.singleData { it.invoke(this); onResult?.invoke(this, null) } }

    if (onResult != null && (onNext == null || onSingle == null)) {
        receiver.singleData { onResult(this, null) }
    } else if (onResult != null && onError == null) {
        receiver.singleError { onResult(null, this) }
    }
}

class BasePresentationDelegate(
    private val target: BasePresentation,
    private val context: Context,
    private val progressBarController: ProgressBarController
) : BasePresentation {

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
        target.showError(message)
    }

    override fun showError(messageRes: Int) {
        target.showError(context string messageRes)
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
        target.showMessage(context string messageRes)
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

    override fun showLoading(isLoading: Boolean) {
        if (isLoading) progressBarController.show() else progressBarController.hide()
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