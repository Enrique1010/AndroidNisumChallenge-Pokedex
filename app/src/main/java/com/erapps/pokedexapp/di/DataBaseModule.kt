package com.erapps.pokedexapp.di

import android.content.Context
import androidx.room.Room
import com.erapps.pokedexapp.data.room.PokeDexAppDataBase
import com.erapps.pokedexapp.data.room.SearchPokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideSearchPokemonDao(pokeDexAppDataBase: PokeDexAppDataBase): SearchPokemonDao {
        return pokeDexAppDataBase.searchPokemonDao()
    }

    @Singleton
    @Provides
    fun providePokeDexDataBase(@ApplicationContext appContext: Context): PokeDexAppDataBase {
        return Room.databaseBuilder(
            appContext,
            PokeDexAppDataBase::class.java,
            "PokeDexApp_DB"
        ).fallbackToDestructiveMigration().build()
    }
}