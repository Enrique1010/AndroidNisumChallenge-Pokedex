package com.erapps.pokedexapp.data.source.remote

import com.erapps.pokedexapp.data.Result
import com.erapps.pokedexapp.data.api.PokeApiService
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import com.erapps.pokedexapp.data.source.mapResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SearchPokemonRemoteDataSource {
    fun getPokemons(): Flow<Result<PokemonListResponse, *>>
}

class SearchPokemonRemoteDataSourceImp @Inject constructor(
    private val apiService: PokeApiService
): SearchPokemonRemoteDataSource {

    override fun getPokemons(): Flow<Result<PokemonListResponse, *>> = mapResponse(Dispatchers.IO) {
        apiService.getPokemonList()
    }
}