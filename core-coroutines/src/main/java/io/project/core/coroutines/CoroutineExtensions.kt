@file:Suppress("EXPERIMENTAL_API_USAGE", "NOTHING_TO_INLINE")
@file:UseExperimental(InternalCoroutinesApi::class)

package io.project.core.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.consumes
import kotlinx.coroutines.channels.produce
import kotlin.coroutines.CoroutineContext


inline fun Throwable.isCoroutineInnerException(): Boolean {
    return this is CancellationException
}

inline fun Result<*>.isCoroutineInnerException(): Boolean {
    return this.exceptionOrNull() is CancellationException
}

inline fun Result<*>.thowCoroutineInnerException() {
    if (isCoroutineInnerException()) {
        throw exceptionOrNull()!!
    }
}

inline fun Result<*>.onSafeCoroutineFailure(action: (exception: Throwable) -> Unit) {
    onFailure { if (!it.isCoroutineInnerException()) action(it) }
}

inline fun Result<*>.safeCoroutineExceptionOrNull(): Throwable? {
    return if (!isCoroutineInnerException()) exceptionOrNull() else null
}

suspend fun <T> ReceiveChannel<T>.subscribe(job: Job, onNext: suspend ReceiveChannel<T>.(T) -> Unit) {
    job.invokeOnCompletion { this.cancel() }
    consumeEach {
        if (job.isCancelled) {
            cancel()
        } else {
            onNext(it)
        }
    }
}

suspend inline fun <T> ReceiveChannel<T>.startWith(item: T, context: CoroutineContext = Dispatchers.Unconfined): ReceiveChannel<T> =
    GlobalScope.produce(context = context, onCompletion = this.consumes()) {
        send(item)
        for (userData in this@startWith) {
            send(userData)
        }
    }

suspend inline fun <T> ReceiveChannel<T>.onNext(
    context: CoroutineContext = Dispatchers.Unconfined,
    crossinline onNext: suspend ReceiveChannel<T>.(T) -> Unit
): ReceiveChannel<T> =
    GlobalScope.produce(context, onCompletion = consumes()) {
        consumeEach { onNext(it) }
    }
