package com.slashgil.marvel.di

import com.slashgil.marvel.data.remote.contract.MarvelRemoteDataSource
import com.slashgil.marvel.data.remote.impl.MarvelRemoteDataSourceImpl
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
