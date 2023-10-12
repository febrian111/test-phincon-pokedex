package com.febri.core_data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pokemon(
    @SerializedName("base_experience")
    val baseExperience: Int,
    val height: Int,
    var id: Int,
    var name: String,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<Type>,
    val weight: Int,
    @SerializedName("dominant_color")
    var dominantColor: Int?,
    var genera: String,
    var description: String,
    @SerializedName("capture_rate")
    var captureRate: Int,
    var url: String? = ""
) : Serializable {

    data class Stat(
        @SerializedName("base_stat")
        val baseStat: Int,
    ) : Serializable

    data class StatX(
        val name: String,
        val url: String
    ) : Serializable

    data class Type(
        val type: TypeX
    ) : Serializable

    data class TypeX(
        val name: String
    ) : Serializable
    data class Sprites(
        val other: Other,
    ) : Serializable
    data class Other(
        @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
    ) : Serializable
    data class OfficialArtwork(
        @SerializedName("front_default")
        val frontDefault: String
    ) : Serializable
}