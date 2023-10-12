package com.febri.core_data

import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults

class DefaultPokedexRepositoryImpl (
    private val remoteDataSource : PokedexDataSource,
    private val localDataSource : PokedexDataSource
) : PokedexRepository() {
    override suspend fun getPokemon(id: Int): Pokemon {
        return if (isPokemonCached(id)) {
            getSinglePokemon(id)
        } else {
            val pokemon = remoteDataSource.getPokemon(id)
            val species = remoteDataSource.getSpecies(id)
            pokemon.genera = getPokemonGenera(species.genera)
            pokemon.description = getPokemonDescription(species.flavorTextEntries)
            pokemon.captureRate = species.captureRate
            pokemon
        }
    }

    override suspend fun getAllPokemonOfType(type: String): PokemonTypeResults {
        return remoteDataSource.getPokemonTypeList(type)
    }

    override suspend fun getAllPokemonNames(limit: Int): PokemonResults {
        return remoteDataSource.getPokemonList(limit)
    }

    override suspend fun addPokemon(pokemon: Pokemon) {
        localDataSource.addPokemon(pokemon)
    }

    override suspend fun catchPokemon(pokemon: Pokemon) {
        localDataSource.capturePokemon(pokemon)
    }

    override fun getSinglePokemon(id: Int): Pokemon {
        return localDataSource.getSinglePokemon(id)
    }

    override fun isPokemonCaptured(id: Int): Boolean {
        return localDataSource.isPokemonCaptured(id)
    }

    override fun isPokemonCached(id: Int): Boolean {
        return localDataSource.isPokemonCached(id)
    }

    override fun getCapturedPokemonList(): List<Pokemon> {
        return localDataSource.getCapturedPokemonList()
    }

    override suspend fun releaseCapturedPokemon(pokemon: Pokemon) {
        localDataSource.releaseCapturedPokemon(pokemon)
    }
}