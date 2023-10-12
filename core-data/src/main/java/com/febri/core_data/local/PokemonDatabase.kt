package com.febri.core_data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.febri.core_data.entity.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {
    abstract fun pokemonDao() : PokemonDao
}