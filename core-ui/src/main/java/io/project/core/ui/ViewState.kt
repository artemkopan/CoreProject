package io.project.core.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.project.core.android.lifecycle.toSingleEvent
import kotlin.LazyThreadSafetyMode.PUBLICATION

interface ViewState<T> {

    fun observeLoading(owner: LifecycleOwner, accept: Boolean.() -> Unit)
    fun singleData(owner: LifecycleOwner, accept: T.() -> Unit)
    fun observeData(owner: LifecycleOwner, accept: T.() -> Unit)
    fun singleError(owner: LifecycleOwner, accept: Throwable.() -> Unit)
    fun observeError(owner: LifecycleOwner, accept: Throwable.() -> Unit)

    fun setLoading(isLoading: Boolean, isImmediately: Boolean = false)
    fun setValue(data: T, isImmediately: Boolean = false)
    fun setError(throwable: Throwable, isImmediately: Boolean = false)

    fun clearLoading(isImmediately: Boolean)
    fun clearData(isImmediately: Boolean)
    fun clearError(isImmediately: Boolean)
    fun clearAll(isImmediately: Boolean)

    companion object {
        fun <T> create(): ViewState<T> = ViewStateImpl()
    }
}

open class ViewStateImpl<T> : ViewState<T> {

    private val data: LiveData<T> by lazy(PUBLICATION) { MutableLiveData<T>() }
    private val error: LiveData<Throwable> by lazy(PUBLICATION) { MutableLiveData<Throwable>() }
    private val loading: LiveData<Boolean> by lazy(PUBLICATION) { MutableLiveData<Boolean>() }

    override fun observeLoading(owner: LifecycleOwner, accept: Boolean.() -> Unit) {
        loading.observe(owner, Observer { it?.let(accept) })
    }

    override fun singleData(owner: LifecycleOwner, accept: T.() -> Unit) {
        data.toSingleEvent().observe(owner, Observer { it?.let(accept) })
    }

    override fun observeData(owner: LifecycleOwner, accept: T.() -> Unit) {
        data.observe(owner, Observer { it?.let(accept) })
    }

    override fun singleError(owner: LifecycleOwner, accept: Throwable.() -> Unit) {
        error.toSingleEvent().observe(owner, Observer { it?.let(accept) })
    }

    override fun observeError(owner: LifecycleOwner, accept: Throwable.() -> Unit) {
        error.observe(owner, Observer { it?.let(accept) })
    }

    override fun setLoading(isLoading: Boolean, isImmediately: Boolean) {
        loading.performLiveData(isImmediately) { isLoading }
    }

    override fun setValue(data: T, isImmediately: Boolean) {
        this.data.performLiveData(isImmediately) { data }
    }

    override fun setError(throwable: Throwable, isImmediately: Boolean) {
        error.performLiveData(isImmediately) { throwable }
    }

    override fun clearLoading(isImmediately: Boolean) {
        loading.performLiveData(isImmediately) { null }
    }

    override fun clearData(isImmediately: Boolean) {
        data.performLiveData(isImmediately) { null }
    }

    override fun clearError(isImmediately: Boolean) {
        error.performLiveData(isImmediately) { null }
    }

    override fun clearAll(isImmediately: Boolean) {
        clearLoading(isImmediately)
        clearData(isImmediately)
        clearError(isImmediately)
    }

    private inline fun <T> LiveData<T>.performLiveData(isImmediately: Boolean, value: () -> T?) {
        (this as MutableLiveData<T>).let {
            if (isImmediately) {
                it.value = value()
            } else {
                it.postValue(value())
            }
        }
    }
}