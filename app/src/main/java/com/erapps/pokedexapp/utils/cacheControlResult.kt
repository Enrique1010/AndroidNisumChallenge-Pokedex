package com.erapps.pokedexapp.utils

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

//class to help handle online/offline caching
inline fun cacheControlResult(
    crossinline query: () -> Flow<List<ShortPokemon>>,
    crossinline fetch: suspend () -> List<ShortPokemon>,
    crossinline saveFetchResult: suspend (List<ShortPokemon>) -> Unit,
    crossinline shouldFetch: (PokemonListEntity?) -> Boolean = { true }
) = flow {
    val data = query().first()

    val dataFlow = if (shouldFetch(PokemonListEntity(pokemonList = data))) {
        emit(Result.Loading)

        try {
            saveFetchResult(fetch())
            query().map { Result.Success(it) }
        }catch (t: Throwable) {
            query().map { Result.Error(data = it, exception = t) }
        }

    } else {
        query().map { Result.Success(it) }
    }

    emitAll(dataFlow)
}.flowOn(Dispatchers.Default)