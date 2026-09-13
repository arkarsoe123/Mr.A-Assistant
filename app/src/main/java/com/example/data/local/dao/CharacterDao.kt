package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CharacterAiEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM character_ais ORDER BY createdAt DESC")
    fun getAllCharacters(): Flow<List<CharacterAiEntity>>

    @Query("SELECT * FROM character_ais WHERE isActive = 1 LIMIT 1")
    fun getActiveCharacterFlow(): Flow<CharacterAiEntity?>

    @Query("SELECT * FROM character_ais WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveCharacter(): CharacterAiEntity?

    @Query("SELECT * FROM character_ais WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: Long): CharacterAiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterAiEntity): Long

    @Update
    suspend fun updateCharacter(character: CharacterAiEntity)

    @Query("UPDATE character_ais SET isActive = 0")
    suspend fun deactivateAll()

    @Query("UPDATE character_ais SET isActive = 1 WHERE id = :id")
    suspend fun activateCharacter(id: Long)

    @Query("SELECT COUNT(*) FROM character_ais")
    suspend fun getCharacterCount(): Int

    @Query("DELETE FROM character_ais WHERE id = :id")
    suspend fun deleteCharacter(id: Long)
}
