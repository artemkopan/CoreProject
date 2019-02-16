package io.project.core.app

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


fun <T> lazyNonSafety(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

fun String?.nullToEmpty() = this ?: ""
fun String?.emptyToNull() = if (isNullOrEmpty()) null else this

fun Any.serialize(): ByteArray {
    ByteArrayOutputStream().use { out ->
        ObjectOutputStream(out).use {
            it.writeObject(this)
            return out.toByteArray()
        }
    }
}

inline fun <reified T> ByteArray.deserialize(): T {
    ByteArrayInputStream(this).use { input ->
        ObjectInputStream(input).use {
            return it.readObject() as T
        }
    }
}