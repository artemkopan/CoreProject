package io.presentation

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import kotlinx.android.synthetic.*

abstract class BaseActivity : AppCompatActivity(), BasePresentation, BasePresentationLifecycleOwner, LifecycleOwner {

    private val basePresentationDelegate by lazy(LazyThreadSafetyMode.NONE) {
        BasePresentationDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (getContentView() != View.NO_ID) {
            setContentView(getContentView())
        }
        onCreated(savedInstanceState)
    }

    override fun onDestroy() {
        clearFindViewByIdCache()
        super.onDestroy()
    }

    override val lifecycleOwner: LifecycleOwner get() = this

    @LayoutRes
    protected abstract fun getContentView(): Int

    protected abstract fun onCreated(savedInstanceState: Bundle?)

    protected open fun showStatusBarOverlay() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    protected open fun hideStatusBarOverlay() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun showError(messageRes: Int) = basePresentationDelegate.showError(messageRes)

    override fun showError(throwable: Throwable?) = basePresentationDelegate.showError(throwable)

    override fun showError(message: String) = basePresentationDelegate.showError(message)

    override fun showMessage(messageRes: Int) = basePresentationDelegate.showMessage(messageRes)

    override fun showMessage(message: String) = basePresentationDelegate.showMessage(message)

    override fun showKeyboard(view: View) = basePresentationDelegate.showKeyboard(view)

    override fun hideKeyboard(view: View?) = basePresentationDelegate.hideKeyboard(view)

}