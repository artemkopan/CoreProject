package io.project.core.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.project.core.android.lifecycle.LiveEvent
import io.project.core.android.lifecycle.toSingleEvent
import io.project.core.app.Logger
import io.project.core.app.Optional
import io.project.core.app.toOptional
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

    val value: Optional<T>
    val isLoading: Optional<Boolean>
    val error: Optional<Throwable>

    companion object {
        fun <T> create(): ViewState<T> = ViewStateImpl()
    }
}

open class ViewStateImpl<T> : ViewState<T> {

    private val valueLiveData: LiveData<T> by lazy(PUBLICATION) { MutableLiveData<T>() }
    private val singleValueLiveData: LiveEvent<T> by lazy(PUBLICATION) { valueLiveData.toSingleEvent() }
    private val errorLiveData: LiveData<Throwable> by lazy(PUBLICATION) { MutableLiveData<Throwable>() }
    private val singleErrorLiveData: LiveEvent<Throwable> by lazy(PUBLICATION) { errorLiveData.toSingleEvent() }
    private val loadingLiveData: LiveData<Boolean> by lazy(PUBLICATION) { MutableLiveData<Boolean>() }

    override fun observeLoading(owner: LifecycleOwner, accept: Boolean.() -> Unit) {
        loadingLiveData.observe(owner, Observer { it?.let(accept) })
    }

    override fun singleData(owner: LifecycleOwner, accept: T.() -> Unit) {
        singleValueLiveData.observe(owner, Observer { it?.let(accept) })
    }

    override fun observeData(owner: LifecycleOwner, accept: T.() -> Unit) {
        valueLiveData.observe(owner, Observer { it?.let(accept) })
    }

    override fun singleError(owner: LifecycleOwner, accept: Throwable.() -> Unit) {
        singleErrorLiveData.observe(owner, Observer { it?.let(accept) })
    }

    override fun observeError(owner: LifecycleOwner, accept: Throwable.() -> Unit) {
        errorLiveData.observe(owner, Observer { it?.let(accept) })
    }

    override fun setLoading(isLoading: Boolean, isImmediately: Boolean) {
        loadingLiveData.performLiveData(isImmediately) { isLoading }
    }

    override fun setValue(data: T, isImmediately: Boolean) {
        this.valueLiveData.performLiveData(isImmediately) { data }
    }

    override fun setError(throwable: Throwable, isImmediately: Boolean) {
        errorLiveData.performLiveData(isImmediately) { throwable }
    }

    override fun clearLoading(isImmediately: Boolean) {
        loadingLiveData.performLiveData(isImmediately) { null }
    }

    override fun clearData(isImmediately: Boolean) {
        valueLiveData.performLiveData(isImmediately) { null }
    }

    override fun clearError(isImmediately: Boolean) {
        errorLiveData.performLiveData(isImmediately) { null }
    }

    override fun clearAll(isImmediately: Boolean) {
        clearLoading(isImmediately)
        clearData(isImmediately)
        clearError(isImmediately)
    }

    override val value: Optional<T>
        get() = valueLiveData.value.toOptional()

    override val isLoading: Optional<Boolean>
        get() = loadingLiveData.value.toOptional()

    override val error: Optional<Throwable>
        get() = errorLiveData.value.toOptional()


    override fun toString(): String {
        return "ViewStateImpl[isLoading: ${loadingLiveData.value}, data: ${valueLiveData.value}, error: ${errorLiveData.value}]"
    }

    private inline fun <T> LiveData<T>.performLiveData(isImmediately: Boolean, valueBlock: () -> T?) {
        (this as MutableLiveData<T>).let {
            val value = valueBlock()
            if (BuildConfig.DEBUG)
                Logger.d("Perform View State: ${this@ViewStateImpl}, value: $value, isImmediately: $isImmediately")
            if (isImmediately) {
                it.value = value
            } else {
                it.postValue(value)
            }
        }
    }
}