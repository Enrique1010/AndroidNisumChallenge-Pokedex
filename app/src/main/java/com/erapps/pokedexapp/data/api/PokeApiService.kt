package com.erapps.pokedexapp.data.api

import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = 150
    ): NetworkResponse<PokemonListResponse, *>

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemon(@Path("pokemonId") pokemonId: String): NetworkResponse<Pokemon, *>
}