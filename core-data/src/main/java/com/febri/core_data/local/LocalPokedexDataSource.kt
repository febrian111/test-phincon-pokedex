package com.febri.core_data.local

import com.febri.core_data.PokedexDataSource
import com.febri.core_data.entity.PokemonItemConverter
import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import com.febri.core_data.model.Species


class LocalPokedexDataSource(private val pokemonDao: PokemonDao) : PokedexDataSource {

    private val pokemonItemConverter = PokemonItemConverter()

    override fun getCapturedPokemonList(): List<Pokemon> {
        return pokemonDao.readFavoriteItems().map {
            pokemonItemConverter.roomPokemonToPokemon(it)
        }
    }

    override fun getAllPokemonList(): List<Pokemon> {
        return pokemonDao.readFavoriteItems().map {
            pokemonItemConverter.roomPokemonToPokemon(it)
        }
    }

    override fun getSinglePokemon(id: Int): Pokemon {
        return pokemonItemConverter.roomPokemonToPokemon(pokemonDao.readSingleItem(id))
    }

    override suspend fun capturePokemon(pokemon: Pokemon) {
        return pokemonDao.addFavoritePokemon(
            pokemonItemConverter.pokemonToRoomPokemon(
                pokemon,
                true
            )
        )
    }

    override suspend fun releaseCapturedPokemon(pokemon: Pokemon) {
        pokemonDao.removeFavoritePokemon(pokemonItemConverter.pokemonToRoomPokemon(pokemon, false))
    }

    override suspend fun addPokemon(pokemon: Pokemon) {
        return pokemonDao.addRoomPokemonItem(pokemonItemConverter.pokemonToRoomPokemon(pokemon))
    }

    override fun isPokemonCaptured(id: Int): Boolean {
        return pokemonDao.isPokemonFavorite(id)
    }

    override fun isPokemonCached(id: Int): Boolean {
        return pokemonDao.isPokemonSaved(id)
    }

    override suspend fun getPokemon(id: Int): Pokemon {
        throw RuntimeException("The getPokemon function should only called from remote data source")
    }

    override suspend fun getSpecies(id: Int): Species {
        throw RuntimeException("The getSpecies function should only called from remote data source")
    }

    override suspend fun getPokemonList(limit: Int): PokemonResults {
        throw RuntimeException("The getPokemonList function should only called from remote data source")
    }

    override suspend fun getPokemonTypeList(type: String): PokemonTypeResults {
        throw RuntimeException("The getPokemonTypeList function should only called from remote data source")
    }
}