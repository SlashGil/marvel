package com.slashgil.marvel.di

import android.content.Context
import androidx.room.Room
import com.slashgil.marvel.data.local.dao.CharacterDao
import com.slashgil.marvel.data.local.db.MarvelDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMarvelDatabase(
        @ApplicationContext context: Context
    ): MarvelDatabase {
        return Room.databaseBuilder(
            context,
            MarvelDatabase::class.java,
            "marvel_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: MarvelDatabase): CharacterDao {
        return database.characterDao()
    }
}
