package com.febri.core_data.remote

import com.febri.core_data.PokedexDataSource
import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import com.febri.core_data.model.Species

class RemotePokedexDataSource(private val apiServices: ApiServices) : PokedexDataSource {
    override suspend fun getPokemon(id: Int): Pokemon {
        return apiServices.getPokemon(id)
    }

    override suspend fun getSpecies(id: Int): Species {
        return apiServices.getSpecies(id)
    }

    override suspend fun getPokemonList(limit: Int): PokemonResults {
        return apiServices.getPokemonList(limit)
    }

    override suspend fun getPokemonTypeList(type: String): PokemonTypeResults {
        return getPokemonTypeList(type)
    }

    override fun getCapturedPokemonList(): List<Pokemon> {
        throw RuntimeException("The getCapturedPokemonList function should only called from local data source")
    }

    override fun getAllPokemonList(): List<Pokemon> {
        throw RuntimeException("The getAllPokemonList function should only called from local data source")
    }

    override fun getSinglePokemon(id: Int): Pokemon {
        throw RuntimeException("The getSinglePokemon function should only called from local data source")
    }

    override suspend fun capturePokemon(pokemon: Pokemon) {
        throw RuntimeException("The capturePokemon function should only called from local data source")
    }

    override suspend fun releaseCapturedPokemon(pokemon: Pokemon) {
        throw RuntimeException("The releaseCapturedPokemon function should only called from local data source")
    }

    override suspend fun addPokemon(pokemon: Pokemon) {
        throw RuntimeException("The addPokemon function should only called from local data source")
    }

    override fun isPokemonCaptured(id: Int): Boolean {
        throw RuntimeException("The isPokemonCaptured function should only called from local data source")
    }

    override fun isPokemonCached(id: Int): Boolean {
        throw RuntimeException("The isPokemonCached function should only called from local data source")
    }
}
