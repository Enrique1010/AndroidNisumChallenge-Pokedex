package com.erapps.pokedexapp.data.source

import com.erapps.pokedexapp.data.api.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.erapps.pokedexapp.data.Result

fun <R : Any, E : Any> mapResponse(
    coroutineDispatcher: CoroutineDispatcher,
    request: suspend () -> NetworkResponse<R, E>
): Flow<Result<R, E>> =
    flow {
        emit(Result.Loading)
        when (val result = request()) {
            is NetworkResponse.Success -> emit(Result.Success(result.body))
            is NetworkResponse.ApiError -> emit(Result.Error(result.body, result.code))
            is NetworkResponse.NetworkError -> emit(Result.Error(exception = result.error))
            is NetworkResponse.UnknownError -> emit(Result.Error(exception = result.error))
        }
    }.flowOn(coroutineDispatcher)