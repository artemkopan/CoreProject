package io.project.core.app

interface DiffEntity<T> {

    fun areItemsTheSame(target: T): Boolean

    fun areContentsTheSame(target: T): Boolean

}