package io.project.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

private typealias KDispatchers = kotlinx.coroutines.Dispatchers

class DispatchersProviderImpl : DispatchersProvider {

    override val io: CoroutineDispatcher
        get() = KDispatchers.IO

    override val main: CoroutineDispatcher
        get() = KDispatchers.Main

    override val unconfined: CoroutineDispatcher
        get() = KDispatchers.Unconfined

}