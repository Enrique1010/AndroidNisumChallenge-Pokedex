package com.erapps.pokedexapp.data.api

import com.erapps.pokedexapp.data.api.models.Pokemon
import com.erapps.pokedexapp.data.api.models.PokemonListResponse
import com.erapps.pokedexapp.data.api.models.abilities.AbilityDetails
import com.erapps.pokedexapp.data.api.models.encounters.PokemonEncounters
import com.erapps.pokedexapp.data.api.models.encounters.locationarea.LocationAreaDetails
import com.erapps.pokedexapp.data.api.models.moves.MoveDetails
import com.erapps.pokedexapp.data.api.models.species.SpeciesDetails
import com.erapps.pokedexapp.data.api.models.species.evolutionchain.EvolutionChainDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("offset") page: Int = 0,
        @Query("limit") limit: Int = 150
    ): NetworkResponse<PokemonListResponse, *>

    @GET("pokemon/{pokemonId}")
    suspend fun getPokemon(@Path("pokemonId") pokemonId: String): NetworkResponse<Pokemon, *>

    //details calls
    @GET
    suspend fun getAbilityDetails(@Url abilityDetailsUrl: String): NetworkResponse<AbilityDetails, *>

    @GET
    suspend fun getMoveDetails(@Url moveDetailsUrl: String): NetworkResponse<MoveDetails, *>

    @GET
    suspend fun getPokemonSpecieDetails(@Url specieDetailsUrl: String): NetworkResponse<SpeciesDetails, *>

    @GET
    suspend fun getEvolutionChain(@Url evolutionChainUrl: String): NetworkResponse<EvolutionChainDetails, *>

    @GET
    suspend fun getEncounters(@Url encountersUrl: String): NetworkResponse<PokemonEncounters, *>

    @GET
    suspend fun getLocationAreas(@Url locationAreaUrl: String): NetworkResponse<LocationAreaDetails, *>

}