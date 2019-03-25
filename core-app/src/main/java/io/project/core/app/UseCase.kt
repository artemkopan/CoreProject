package io.project.core.app


interface UseCase<Result> {
    fun execute(): Result
}

interface UseCaseParams<Params, Result> {
    fun execute(params: Params): Result
}

interface UseCaseBiParams<Params1, Params2, Result> {
    fun execute(params1: Params1, params2: Params2): Result
}

interface UseCaseParams3<Params1, Params2, Params3, Result> {
    fun execute(params1: Params1, params2: Params2, params3: Params3): Result
}

interface UseCaseAsync<Result> {
    suspend fun execute(): Result
}

interface UseCaseAsyncParams<Params, Result> {
    suspend fun execute(params: Params): Result
}

interface UseCaseAsyncBiParams<Params1, Params2, Result> {
    suspend fun execute(params1: Params1, params2: Params2): Result
}