package io.project.core.app

import androidx.annotation.FloatRange
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream


fun <T> lazyNonSafety(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

inline fun CharSequence?.toStringOrEmpty() = this?.toString().orEmpty()
inline fun String?.nullToEmpty() = this ?: ""
inline fun String?.emptyToNull() = if (isNullOrEmpty()) null else this

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

inline fun Double?.orZero() = this ?: 0.0
inline fun Float?.orZero() = this ?: 0f
inline fun Int?.orZero() = this ?: 0

inline fun CharSequence?.toDoubleOrZero() = toStringOrEmpty().toDoubleOrNull().orZero()
inline fun CharSequence?.toIntOrZero() = toStringOrEmpty().toIntOrNull().orZero()

inline fun Int.convertColorToHex() = String.format("#%06X", 0xFFFFFF and this)

fun lerp(from: Float, to: Float, @FloatRange(from = 0.0, to = 1.0) fraction: Float): Float {
    return from + fraction * (to - from)
}

fun Map<*, *>.log() = this.keys.joinToString { "Key: $it | Data: ${get(it)}" }
