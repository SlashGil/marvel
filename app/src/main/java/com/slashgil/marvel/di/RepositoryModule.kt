package com.slashgil.marvel.di

import com.slashgil.marvel.data.repository.MarvelRepositoryImpl
import com.slashgil.marvel.domain.repository.MarvelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMarvelRepository(
        marvelRepositoryImpl: MarvelRepositoryImpl
    ): MarvelRepository
}
