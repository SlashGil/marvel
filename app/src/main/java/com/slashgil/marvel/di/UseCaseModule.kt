package com.slashgil.marvel.di

import com.slashgil.marvel.domain.usecase.GetCharacterDetailsUseCase
import com.slashgil.marvel.domain.usecase.GetCharacterDetailsUseCaseImpl
import com.slashgil.marvel.domain.usecase.GetCharactersUseCase
import com.slashgil.marvel.domain.usecase.GetCharactersUseCaseImpl
import com.slashgil.marvel.domain.usecase.GetComicsForCharacterUseCase
import com.slashgil.marvel.domain.usecase.GetComicsForCharacterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun bindGetCharactersUseCase(
        impl: GetCharactersUseCaseImpl
    ): GetCharactersUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetCharacterDetailsUseCase(
        impl: GetCharacterDetailsUseCaseImpl
    ): GetCharacterDetailsUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetComicsForCharacterUseCase(
        impl: GetComicsForCharacterUseCaseImpl
    ): GetComicsForCharacterUseCase
}
