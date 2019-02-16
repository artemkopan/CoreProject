package io.core.common

import java.io.Serializable

class Optional<T> private constructor(val data: T?) : Serializable {

    fun isEmpty() = data == null
    fun isNotEmpty() = !isEmpty()

    fun get(default: T) = data ?: default

    companion object {
        fun <T> empty(): Optional<T> = Optional(null)
        fun <T> of(t: T?): Optional<T> = if (t == null) empty() else Optional(t)
    }

}

fun <T> T?.toOptional() = Optional.of(this)
