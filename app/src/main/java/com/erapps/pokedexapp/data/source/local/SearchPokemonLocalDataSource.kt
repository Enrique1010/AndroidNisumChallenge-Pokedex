package com.erapps.pokedexapp.data.source.local

import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.SearchPokemonDao
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SearchPokemonLocalDataSource {
    suspend fun insertPokemons(pokemonListEntity: PokemonListEntity)
    fun getCachedPokemons(): Flow<List<ShortPokemon>>
    suspend fun clearPokemons()
}

class SearchPokemonLocalDataSourceImp @Inject constructor(
    private val searchPokemonDao: SearchPokemonDao
) : SearchPokemonLocalDataSource {

    override suspend fun insertPokemons(pokemonListEntity: PokemonListEntity) =
        withContext(Dispatchers.IO) {
            searchPokemonDao.insertPokemons(pokemonListEntity)
        }

    override fun getCachedPokemons(): Flow<List<ShortPokemon>> = flow {
        emit(searchPokemonDao.getCachedPokemons()?.pokemonList ?: emptyList())
    }.flowOn(Dispatchers.IO)

    override suspend fun clearPokemons() = withContext(Dispatchers.IO) {
        searchPokemonDao.clearPokemons()
    }

}