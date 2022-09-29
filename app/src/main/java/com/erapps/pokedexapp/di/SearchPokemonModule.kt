package com.erapps.pokedexapp.di

import com.erapps.pokedexapp.data.source.SearchPokemonRepository
import com.erapps.pokedexapp.data.source.SearchPokemonRepositoryImp
import com.erapps.pokedexapp.data.source.remote.SearchPokemonRemoteDataSource
import com.erapps.pokedexapp.data.source.remote.SearchPokemonRemoteDataSourceImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchPokemonModule {

    @Singleton
    @Binds
    abstract fun provideSearchPokemonRemoteDataSource(
        searchPokemonRemoteDataSourceImp: SearchPokemonRemoteDataSourceImp
    ): SearchPokemonRemoteDataSource

    @Singleton
    @Binds
    abstract fun provideSearchPokemonRepository(
        searchPokemonRepositoryImp: SearchPokemonRepositoryImp
    ): SearchPokemonRepository
}