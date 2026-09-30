package com.slashgil.marvel.di

import com.slashgil.marvel.data.local.contract.MarvelLocalDataSource
import com.slashgil.marvel.data.local.impl.MarvelLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMarvelLocalDataSource(
        impl: MarvelLocalDataSourceImpl
    ): MarvelLocalDataSource
}
