package io.core.common

interface DiffEntity<T> {

    fun areItemsTheSame(target: T): Boolean

    fun areContentsTheSame(target: T): Boolean

}