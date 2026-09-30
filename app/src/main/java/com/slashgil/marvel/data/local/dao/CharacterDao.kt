package com.slashgil.marvel.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slashgil.marvel.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<CharacterEntity>)

    @Query("""
        SELECT * FROM characters 
        WHERE (:query = '' OR name LIKE '%' || :query || '%' OR fullName LIKE '%' || :query || '%')
        AND (:publisher = '' OR :publisher = 'All' OR publisher LIKE '%' || :publisher || '%')
        ORDER BY name ASC
    """)
    fun searchCharacters(query: String, publisher: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getCharacterById(id: String): CharacterEntity?

    @Query("SELECT COUNT(*) FROM characters")
    fun getCharacterCount(): Int
}
