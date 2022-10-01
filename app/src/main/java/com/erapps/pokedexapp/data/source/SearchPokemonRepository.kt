package com.erapps.pokedexapp.data.source

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import com.erapps.pokedexapp.data.source.local.SearchPokemonLocalDataSource
import com.erapps.pokedexapp.data.source.remote.SearchPokemonRemoteDataSource
import com.erapps.pokedexapp.utils.cacheControlResult
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface SearchPokemonRepository {
    //remote and local
    fun getPokemons(): Flow<Result<List<ShortPokemon>, *>>
}

class SearchPokemonRepositoryImp @Inject constructor(
    private val remoteDataSource: SearchPokemonRemoteDataSource,
    private val localDataSource: SearchPokemonLocalDataSource
) : SearchPokemonRepository {

    //remote and local
    override fun getPokemons(): Flow<Result<List<ShortPokemon>, *>> =
        cacheControlResult(
            query = {
                localDataSource.getCachedPokemons()
            },
            fetch = {
                var list = emptyList<ShortPokemon>()
                remoteDataSource.getPokemons().collect {
                    list = when (it) {
                        is Result.Error -> {
                            emptyList()
                        }
                        is Result.Success -> {
                            it.data?.results ?: emptyList()
                        }
                        else -> {
                            emptyList()
                        }
                    }
                }
                list
            }, saveFetchResult = {
                localDataSource.clearPokemons()
                localDataSource.insertPokemons(PokemonListEntity(pokemonList = it))

            }, shouldFetch = { pokemonEntity ->
                var oldestTimestamp = System.currentTimeMillis()
                pokemonEntity?.let {
                    oldestTimestamp = it.lastDateUpdated
                }
                val needsRefresh =
                    oldestTimestamp < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(60)
                            || pokemonEntity?.pokemonList.isNullOrEmpty()
                needsRefresh
            })

}