package io.project.core.app


interface UseCase<Params, Result> {
    fun execute(params: Params): Result
}

interface UseCaseBiParams<Params1, Params2, Result> {
    fun execute(params1: Params1, params2: Params2): Result
}

interface UseCaseParams3<Params1, Params2, Params3, Result> {
    fun execute(params1: Params1, params2: Params2, params3: Params3): Result
}

interface UseCaseAsync<Params, Result> {
    suspend fun execute(params: Params): Result
}