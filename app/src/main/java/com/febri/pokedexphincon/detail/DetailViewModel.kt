package com.febri.pokedexphincon.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febri.core_data.PokedexRepository
import com.febri.core_data.model.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: PokedexRepository) : ViewModel() {

    fun addFavoritePokemon(pokemon : Pokemon){
        viewModelScope.launch {
            repository.catchPokemon(pokemon)
        }
    }

    fun deleteFavoritePokemon(pokemon : Pokemon){
        viewModelScope.launch {
            repository.releaseCapturedPokemon(pokemon)
        }
    }

    fun isPokemonFavorite(id : Int) : Boolean{
        return repository.isPokemonCaptured(id)
    }
}
