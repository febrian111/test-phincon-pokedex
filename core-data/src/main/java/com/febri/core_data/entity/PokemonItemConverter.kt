package com.febri.core_data.entity

import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.Pokemon.Sprites
import com.febri.core_data.model.Pokemon.Other
import com.febri.core_data.model.Pokemon.OfficialArtwork
import com.febri.core_data.model.Pokemon.Type
import com.febri.core_data.model.Pokemon.TypeX
import com.febri.core_data.model.Pokemon.Stat


class PokemonItemConverter {

    fun pokemonToRoomPokemon(pokemon: Pokemon, isFavorite: Boolean = false): PokemonEntity {
        return PokemonEntity(
            pokemon.id,
            pokemon.baseExperience,
            pokemon.height,
            pokemon.name,
            getPokemonSpritesString(pokemon.sprites),
            getPokemonStatsString(pokemon.stats),
            getPokemonTypesString(pokemon.types),
            pokemon.weight,
            pokemon.dominantColor,
            pokemon.genera,
            pokemon.description,
            pokemon.captureRate,
            isFavorite
        )
    }

    fun roomPokemonToPokemon(roomPokemon: PokemonEntity) : Pokemon{
        return Pokemon(
            roomPokemon.baseExperience,
            roomPokemon.height,
            roomPokemon.id,
            roomPokemon.name,
            getPokemonSprites(roomPokemon.sprites),
            getPokemonStats(roomPokemon.stats),
            getPokemonTypes(roomPokemon.types),
            roomPokemon.weight,
            roomPokemon.dominantColor,
            roomPokemon.genera,
            roomPokemon.description,
            roomPokemon.captureRate
        )
    }

    private fun getPokemonSpritesString(sprites: Sprites) : String {
        return sprites.other.officialArtwork.frontDefault
    }

    private fun getPokemonTypesString(types: List<Type>) : String {
        val typesString = StringBuilder("")
        for(i in types.indices) {
            typesString.append(types[i].type.name)
            if(i != types.size-1){
                typesString.append(",")
            }
        }
        return typesString.toString()
    }

    private fun getPokemonStatsString(stats : List<Stat>) : String{
        val statsString = StringBuilder("")
        for(i in stats.indices) {
            statsString.append(stats[i].baseStat.toString())
            if(i != stats.size-1){
                statsString.append(",")
            }
        }
        return statsString.toString()
    }

    private fun getPokemonSprites(spritesString: String) : Sprites {
        return Sprites(Other(OfficialArtwork(spritesString)))
    }

    private fun getPokemonTypes(typesString: String) : List<Type> {
        val typesStringList = typesString.split(",")
        val typesList = mutableListOf<Type>()
        typesStringList.forEach {
            typesList.add(Type(TypeX(it)))
        }
        return typesList
    }

    private fun getPokemonStats(statsString: String) : List<Stat>{
        val statsStringList = statsString.split(",")
        val statsList = mutableListOf<Stat>()
        statsStringList.forEach {
            statsList.add(Stat(it.toInt()))
        }
        return statsList
    }
}