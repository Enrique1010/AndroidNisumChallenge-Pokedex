package com.erapps.pokedexapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.erapps.pokedexapp.data.room.entities.PokemonListEntity

@Database(
    version = 1,
    entities = [PokemonListEntity::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokeDexAppDataBase : RoomDatabase() {
    abstract fun searchPokemonDao(): SearchPokemonDao
}