package com.erapps.pokedexapp.data.source

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import com.erapps.pokedexapp.data.source.local.SearchPokemonLocalDataSource
import com.erapps.pokedexapp.data.source.remote.SearchPokemonRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface SearchPokemonRepository {
    //remote
    fun getPokemons(): Flow<Result<PokemonListResponse, *>>

    //local
    suspend fun insertPokemons(pokemonListEntity: PokemonListEntity)
    fun getCachedPokemons(): Flow<List<ShortPokemon>>
    suspend fun getTimeMillis(): Long
    suspend fun clearPokemons()
}

class SearchPokemonRepositoryImp @Inject constructor(
    private val remoteDataSource: SearchPokemonRemoteDataSource,
    private val localDataSource: SearchPokemonLocalDataSource
) : SearchPokemonRepository {

    //remote
    override fun getPokemons(): Flow<Result<PokemonListResponse, *>> {
        return remoteDataSource.getPokemons().flowOn(Dispatchers.Default)
    }

    //local
    override suspend fun insertPokemons(pokemonListEntity: PokemonListEntity) {
        localDataSource.insertPokemons(pokemonListEntity)
    }

    override fun getCachedPokemons(): Flow<List<ShortPokemon>> {
        return localDataSource.getCachedPokemons().flowOn(Dispatchers.Default)
    }

    override suspend fun getTimeMillis(): Long {
        return localDataSource.getTimeMillis()
    }

    override suspend fun clearPokemons() {
        localDataSource.clearPokemons()
    }

}