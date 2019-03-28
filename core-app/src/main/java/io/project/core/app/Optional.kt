package io.project.core.app

import java.io.Serializable

class Optional<T> private constructor(val data: T?) : Serializable {

    fun isEmpty() = data == null
    fun isNotEmpty() = !isEmpty()

    fun get(default: () -> T) = data ?: default()
    fun <M> getAndMap(transform: (T) -> M, default: () -> M) = data?.let(transform) ?: default()
    suspend fun getAsync(default: suspend () -> T) = data ?: default()
    suspend fun <M> getAndMapAsync(transform: suspend (T) -> M, default: suspend () -> M): M {
        return data?.let { transform(it) } ?: default()
    }

    fun getOrThrow(message: () -> String = { "Data is null" }) = data ?: throw NullPointerException(message())

    companion object {
        private val OPTIONAL_EMPTY = Optional(null)

        fun <T> empty(): Optional<T> = OPTIONAL_EMPTY as Optional<T>
        fun <T> of(t: T?): Optional<T> = if (t == null) empty() else Optional(t)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Optional<*>) {
            if (data == null && other.data == null) {
                true
            } else {
                data == other.data
            }
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return data?.hashCode() ?: 0
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