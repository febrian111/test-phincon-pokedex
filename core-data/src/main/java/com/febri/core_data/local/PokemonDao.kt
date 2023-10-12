package com.febri.core_data.local

import androidx.room.*
import com.febri.core_data.entity.PokemonEntity

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRoomPokemonItem(roomPokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_list WHERE is_favorite = 1 ORDER BY id ASC")
    fun readFavoriteItems(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_list ORDER BY id ASC")
    fun readAllItems(): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_list WHERE id = :id")
    fun readSingleItem(id: Int): PokemonEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoritePokemon(pokemon: PokemonEntity)

    @Update
    suspend fun removeFavoritePokemon(pokemon: PokemonEntity)

    @Query("SELECT EXISTS(SELECT * FROM pokemon_list WHERE id = :id AND is_favorite = 1)")
    fun isPokemonFavorite(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT * FROM pokemon_list WHERE id = :id)")
    fun isPokemonSaved(id: Int): Boolean
}