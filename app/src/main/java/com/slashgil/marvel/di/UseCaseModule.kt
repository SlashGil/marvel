package com.slashgil.marvel.di

import com.slashgil.marvel.domain.contract.GetCharacterDetailsUseCase
import com.slashgil.marvel.domain.contract.GetCharactersUseCase
import com.slashgil.marvel.domain.impl.GetCharacterDetailsUseCaseImpl
import com.slashgil.marvel.domain.impl.GetCharactersUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindGetCharactersUseCase(
        impl: GetCharactersUseCaseImpl
    ): GetCharactersUseCase

    @Binds
    @Singleton
    abstract fun bindGetCharacterDetailsUseCase(
        impl: GetCharacterDetailsUseCaseImpl
    ): GetCharacterDetailsUseCase
}
