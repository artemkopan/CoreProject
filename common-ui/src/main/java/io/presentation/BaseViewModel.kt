package io.presentation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel(), BaseViewModelDelegate, LifecycleOwner {

    @Suppress("LeakingThis") //LifecycleRegistry wraps "this" to WeakReference
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun onCleared() {
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

}