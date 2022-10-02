package com.erapps.pokedexapp.di

import com.erapps.pokedexapp.data.source.PokemonDetailsRepository
import com.erapps.pokedexapp.data.source.PokemonDetailsRepositoryImp
import com.erapps.pokedexapp.data.source.remote.PokemonDetailsRemoteDataSource
import com.erapps.pokedexapp.data.source.remote.PokemonDetailsRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PokemonDetailsModule {

    @Singleton
    @Binds
    abstract fun providePokemonDetailsRemoteDataSource(
        pokemonDetailsRemoteDataSourceImp: PokemonDetailsRemoteDataSourceImp
    ): PokemonDetailsRemoteDataSource

    @Singleton
    @Binds
    abstract fun providePokemonDetailsRepository(
        pokemonDetailsRepositoryImp: PokemonDetailsRepositoryImp
    ): PokemonDetailsRepository
}