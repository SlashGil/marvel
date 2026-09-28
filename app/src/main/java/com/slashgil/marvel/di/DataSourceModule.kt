package com.slashgil.marvel.di

import com.slashgil.marvel.data.datasource.remote.MarvelRemoteDataSource
import com.slashgil.marvel.data.datasource.remote.MarvelRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindMarvelRemoteDataSource(
        remoteDataSourceImpl: MarvelRemoteDataSourceImpl
    ): MarvelRemoteDataSource
}
