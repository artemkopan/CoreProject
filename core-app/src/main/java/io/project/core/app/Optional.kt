package io.project.core.app

import java.io.Serializable

class Optional<T> private constructor(val data: T?) : Serializable {

    fun isEmpty() = data == null
    fun isNotEmpty() = !isEmpty()

    fun get(default: () -> T) = data ?: default()
    suspend fun getAsync(default: suspend () -> T) = data ?: default()

    fun getOrThrow(message: String = "Data is null") = data ?: throw NullPointerException(message)

    companion object {
        fun <T> empty(): Optional<T> = Optional(null)
        fun <T> of(t: T?): Optional<T> = if (t == null) empty() else Optional(t)
    }

}

fun <T> T?.toOptional() = Optional.of(this)

fun <T> Optional<T>.fold(onResult: (T) -> Unit, onEmpty: () -> Unit) {
    if (isNotEmpty()) {
        onResult(data!!)
    } else {
        onEmpty()
    }
}