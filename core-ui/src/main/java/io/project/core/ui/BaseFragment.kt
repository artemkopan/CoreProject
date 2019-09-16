package io.project.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.project.core.app.lazyNonSafety

abstract class BaseFragment : Fragment(), BasePresentation, PresentationLifecycle {

    protected abstract val layoutRes: Int
    protected abstract val progressBarController: ProgressBarController

    private val basePresentationDelegate by lazyNonSafety {
        BasePresentationDelegate(this, requireContext(), progressBarController)
    }

    override val presentationLifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    override val instanceLifecycleOwner: LifecycleOwner
        get() = this

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return (if (layoutRes != View.NO_ID) {
            inflater.inflate(layoutRes, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }).also {
            progressBarController.onViewCreated()
        }
    }

    override fun showError(messageRes: Int) = basePresentationDelegate.showError(messageRes)

    override fun showError(throwable: Throwable?) = basePresentationDelegate.showError(throwable)

    override fun showError(message: String) = basePresentationDelegate.showError(message)

    override fun showMessage(messageRes: Int) = basePresentationDelegate.showMessage(messageRes)

    override fun showMessage(message: String) = basePresentationDelegate.showMessage(message)

    override fun showKeyboard(view: View) = basePresentationDelegate.showKeyboard(view)

    override fun hideKeyboard(view: View?) = basePresentationDelegate.hideKeyboard(view)

    override fun showLoading(isLoading: Boolean) = basePresentationDelegate.showLoading(isLoading)

    protected fun <T> LiveData<T>.subscribe(consumer: (T) -> Unit) {
        this.observe(presentationLifecycleOwner, Observer { it?.let(consumer) })
    }

}
