package com.febri.core_data

import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import com.febri.core_data.model.Species.FlavorTextEntry
import com.febri.core_data.model.Species.Genera

abstract class PokedexRepository {
    abstract suspend fun getPokemon(id: Int): Pokemon

    abstract suspend fun getAllPokemonOfType(type: String): PokemonTypeResults

    abstract suspend fun getAllPokemonNames(limit: Int): PokemonResults

    abstract suspend fun addPokemon(pokemon: Pokemon)

    abstract suspend fun catchPokemon(pokemon : Pokemon)

    abstract fun getSinglePokemon(id: Int): Pokemon

    abstract fun isPokemonCaptured(id : Int) : Boolean

    fun getPokemonGenera(generaList: List<Genera>?): String {
        var index = 0
        while (generaList?.get(index)?.language?.name != "en") {
            index++
        }
        return generaList[index].genus
    }

    abstract fun isPokemonCached(id: Int): Boolean

    abstract fun getCapturedPokemonList() : List<Pokemon>

    abstract suspend fun releaseCapturedPokemon(pokemon: Pokemon)

    fun getPokemonDescription(flavorTextList: List<FlavorTextEntry>): String {
        var index = flavorTextList.size - 1
        while (flavorTextList[index].language.name != "en") {
            index--
        }
        var flavorText = flavorTextList[index].flavorText
        flavorText = flavorText.replace("POKéMON", "Pokémon")
        flavorText = flavorText.replace("\n", " ")
        return flavorText
    }

}