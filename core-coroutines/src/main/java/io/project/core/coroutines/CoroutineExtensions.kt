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

suspend fun <T> ReceiveChannel<T>.distinctUntilChanged(context: CoroutineContext = Dispatchers.Unconfined): ReceiveChannel<T> =
    GlobalScope.produce(context, onCompletion = consumes()) {
        var prev: T? = null
        for (current in this@distinctUntilChanged) {
            if (current != prev) {
                send(current)
                prev = current
            }
        }
    }


suspend fun <T> ReceiveChannel<T>.distinctUntilChanged(
    context: CoroutineContext = Dispatchers.Unconfined,
    comparator: (T, T) -> Boolean
): ReceiveChannel<T> =
    coroutineScope {
        produce(context) {
            var prev: T = receive()
            send(prev)

            consumeEach {
                if (!comparator(it, prev)) {
                    send(it)
                    prev = it
                }
            }
        }
    }

/**
 * @function - zip: Combine items from two Deferreds together via a specified function and return items based on the results of this function
 * @param source1 - first deferred, whose result will be used in zipper
 * @param source2 - second deferred, whose result will be used in zipper
 * @param zipper - function, that will be called with deferred's results
 */
suspend fun <T1, T2, R> zip(source1: Deferred<T1>, source2: Deferred<T2>, coroutineStart: CoroutineStart = CoroutineStart.DEFAULT, zipper: (T1, T2) -> R): Deferred<R> =
    coroutineScope {
        async(start = coroutineStart) {
            zipper(source1.await(), source2.await())
        }
    }

fun <T> ReceiveChannel<T>.debounce(
    wait: Long = 300,
    context: CoroutineContext = Dispatchers.Unconfined,
    scope: CoroutineScope = GlobalScope
): ReceiveChannel<T> = scope.produce(context) {
    var nextTime = 0L
    consumeEach {
        val curTime = System.currentTimeMillis()
        if (curTime < nextTime) {
            // not enough time passed from last send
            delay(nextTime - curTime)
            var mostRecent = it
            while (!isEmpty) {
                mostRecent = receive()
            } // take the most recently sent without waiting
            nextTime += wait // maintain strict time interval between sends
            send(mostRecent)
        } else {
            // big pause between original events
            nextTime = curTime + wait // start tracking time interval from scratch
            send(it)
        }
    }
}
