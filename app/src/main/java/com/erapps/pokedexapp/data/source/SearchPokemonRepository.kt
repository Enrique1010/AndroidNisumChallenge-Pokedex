package com.erapps.pokedexapp.data.source

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import com.erapps.pokedexapp.data.source.remote.SearchPokemonRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface SearchPokemonRepository {
    fun getPokemons(): Flow<Result<PokemonListResponse, *>>
}

class SearchPokemonRepositoryImp @Inject constructor(
    private val dataSource: SearchPokemonRemoteDataSource
) : SearchPokemonRepository {

    override fun getPokemons(): Flow<Result<PokemonListResponse, *>> {
        return dataSource.getPokemons().flowOn(Dispatchers.Default)
    }

}