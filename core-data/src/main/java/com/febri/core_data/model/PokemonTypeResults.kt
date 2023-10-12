package com.febri.core_data.model

import com.google.gson.annotations.SerializedName

data class PokemonTypeResults(@SerializedName("pokemon") val results: List<TypePokemon>) {
    data class TypePokemon(@SerializedName("pokemon") val pokemon: Pokemon)
}