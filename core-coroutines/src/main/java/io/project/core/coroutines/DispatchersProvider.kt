package io.project.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

interface DispatchersProvider {

    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val unconfined: CoroutineDispatcher

}