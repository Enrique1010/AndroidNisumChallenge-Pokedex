package com.erapps.pokedexapp.data.api

import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    fun getPokemonList(
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = 150
    ): NetworkResponse<PokemonListResponse, *>

    @GET("pokemon/{pokemonId}")
    fun getPokemon(@Path("pokemonId") pokemonId: String)
}