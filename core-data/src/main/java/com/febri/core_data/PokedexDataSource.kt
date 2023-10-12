package com.febri.core_data

import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import com.febri.core_data.model.Species

interface PokedexDataSource {

    suspend fun getPokemon(id: Int): Pokemon
    suspend fun getSpecies(id: Int): Species
    suspend fun getPokemonList(limit: Int): PokemonResults
    suspend fun getPokemonTypeList(type : String) : PokemonTypeResults

    fun getCapturedPokemonList(): List<Pokemon>
    fun getAllPokemonList(): List<Pokemon>
    fun getSinglePokemon(id: Int): Pokemon
    suspend fun capturePokemon(pokemon: Pokemon)
    suspend fun releaseCapturedPokemon(pokemon: Pokemon)
    suspend fun addPokemon(pokemon: Pokemon)
    fun isPokemonCaptured(id: Int): Boolean
    fun isPokemonCached(id: Int): Boolean
}