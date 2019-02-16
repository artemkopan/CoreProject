package io.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

abstract class BaseFragment : Fragment(), BasePresentation, BasePresentationLifecycleOwner {

    protected abstract val layoutRes: Int

    private val basePresentationDelegate by lazy(LazyThreadSafetyMode.NONE) {
        BasePresentationDelegate(requireContext())
    }

    override val lifecycleOwner: LifecycleOwner
        get() = viewLifecycleOwner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (layoutRes != View.NO_ID) {
            inflater.inflate(layoutRes, container, false)
        } else {
            super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    override fun showError(messageRes: Int) = basePresentationDelegate.showError(messageRes)

    override fun showError(throwable: Throwable?) = basePresentationDelegate.showError(throwable)

    override fun showError(message: String) = basePresentationDelegate.showError(message)

    override fun showMessage(messageRes: Int) = basePresentationDelegate.showMessage(messageRes)

    override fun showMessage(message: String) = basePresentationDelegate.showMessage(message)

    override fun showKeyboard(view: View) = basePresentationDelegate.showKeyboard(view)

    override fun hideKeyboard(view: View?) = basePresentationDelegate.hideKeyboard(view)

}
