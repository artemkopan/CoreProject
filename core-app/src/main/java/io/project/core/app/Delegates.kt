package io.project.core.app

import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Delegates {

    inline fun <T> observeChanges(initialValue: T, crossinline onChange: (newValue: T) -> Unit):
            ReadWriteProperty<Any?, T> =
        object : ObservableProperty<T>(initialValue) {
            override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) {
                if (oldValue != newValue) {
                    onChange(newValue)
                }
            }
        }

    inline fun <T> onceRead(initialValue: T): ReadWriteProperty<Any?, T> =
        object : ReadWriteProperty<Any?, T> {
            private var value: T = initialValue


            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return value.also { this.value = initialValue }
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                this.value = value
            }
        }
}