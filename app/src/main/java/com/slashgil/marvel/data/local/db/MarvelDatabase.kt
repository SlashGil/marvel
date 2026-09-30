package com.slashgil.marvel.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.slashgil.marvel.data.local.dao.CharacterDao
import com.slashgil.marvel.data.local.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
abstract class MarvelDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
