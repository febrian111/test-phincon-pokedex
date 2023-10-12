package com.febri.core_data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Species(
    var genera: List<Genera>,
    @SerializedName("flavor_text_entries")
    var flavorTextEntries: List<FlavorTextEntry>,
    @SerializedName("captureRate")
    var captureRate : Int
) : Serializable {
    data class FlavorTextEntry(
        @SerializedName("flavor_text")
        val flavorText: String,
        val language : Language
    ) : Serializable
    data class Genera(
        val genus: String,
        val language: Language
    ) : Serializable
    data class Language (
        val name : String
    ) : Serializable
}