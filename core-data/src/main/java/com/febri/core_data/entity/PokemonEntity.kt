package com.febri.core_data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_list")
data class PokemonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo("base_experience")
    val baseExperience: Int,
    val height: Int,
    var name: String,
    val sprites: String,
    val stats: String,
    val types: String,
    val weight: Int,
    @ColumnInfo("dominant_color")
    var dominantColor: Int?,
    val genera: String,
    val description: String,
    @ColumnInfo("capture_rate")
    var captureRate: Int,
    @ColumnInfo("is_favorite")
    var isFavorite: Boolean = false,
)