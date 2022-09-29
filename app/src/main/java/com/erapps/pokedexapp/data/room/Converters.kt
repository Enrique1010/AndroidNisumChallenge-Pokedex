package com.erapps.pokedexapp.data.room

import androidx.room.TypeConverter
import com.erapps.pokedexapp.data.api.models.ShortPokemon
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Converters {
    private val gson = Gson()

    //lists and entities
    @TypeConverter
    fun pokemonSearchEntityToString(pokemonListEntity: PokemonListEntity): String? {
        return gson.toJson(pokemonListEntity)
    }

    @TypeConverter
    fun stringToPokemonSearchEntity(json: String): PokemonListEntity? {
        return gson.fromJson(json, PokemonListEntity::class.java)
    }

    @TypeConverter
    fun pokemonListToString(pokemonList: List<ShortPokemon>): String? {
        return gson.toJson(pokemonList)
    }

    @TypeConverter
    fun stringToPokemonList(json: String): List<ShortPokemon>? {
        if (json.isEmpty()) return emptyList()

        val listType = object : TypeToken<List<ShortPokemon>>() {}.type
        return gson.fromJson(json, listType)
    }
    //objects
    @TypeConverter
    fun shortPokemonToString(shortPokemon: ShortPokemon): String? {
        return gson.toJson(shortPokemon)
    }

    @TypeConverter
    fun stringToShortPokemon(json: String): ShortPokemon? {

        return gson.fromJson(json, ShortPokemon::class.java)
    }
}