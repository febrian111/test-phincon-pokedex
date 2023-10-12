package com.febri.core_data.remote

import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import com.febri.core_data.model.Species
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

    @GET("pokemon/{id}")
    suspend fun getPokemon(
        @Path("id") id: Int
    ): Pokemon

    @GET("pokemon-species/{id}")
    suspend fun getSpecies(
        @Path("id") id: Int
    ): Species

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("limit") limit: Int
    ): PokemonResults

    @GET("type/{type}")
    suspend fun getPokemonTypeList(
        @Path("type") type: String
    ): PokemonTypeResults
}