package com.febri.pokedexphincon.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febri.core_data.PokedexRepository
import com.febri.core_data.model.Pokemon
import com.febri.core_data.model.PokemonResults
import com.febri.core_data.model.PokemonTypeResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.febri.core_data.util.Result
import com.febri.core_data.util.Constants

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PokedexRepository) : ViewModel()  {
    private var coroutineExceptionHandler: CoroutineExceptionHandler
    private var job: Job = Job()
    val list: MutableList<Pokemon> = mutableListOf()

    private val _myPokemon: MutableLiveData<Result<MutableList<Pokemon>>> = MutableLiveData()
    val myPokemon: LiveData<Result<MutableList<Pokemon>>>
        get() = _myPokemon

    private val _myTypePokemon: MutableLiveData<Result<PokemonTypeResults>> = MutableLiveData()
    val myTypePokemon: LiveData<Result<PokemonTypeResults>>
        get() = _myTypePokemon

    private val _myPokemonNamesList: MutableLiveData<Result<PokemonResults>> = MutableLiveData()
    val myPokemonNamesList: LiveData<Result<PokemonResults>>
        get() = _myPokemonNamesList

    init {
        coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
            _myPokemon.value = Result.Failure(exception)
        }
//        pokemonsDisplayed = 0
        getAllPokemonNames()
    }

    fun getPokemon(pokemonList: MutableList<Pokemon>) {
        cancelJobIfRunning()
        job = viewModelScope.launch(coroutineExceptionHandler) {
            _myPokemon.value = Result.Loading
            coroutineScope {
                pokemonList.forEach {
                    launch(coroutineExceptionHandler) {
                        if (!checkIfContainsPokemon(list, it)) {
                            val pokemon = repository.getPokemon(it.id)
                            list.add(pokemon)
                            viewModelScope.launch {
                                repository.addPokemon(pokemon)
                            }
                        }
                    }
                }
            }
            list.sortBy { it.id }
            _myPokemon.value = Result.Success(list)
        }
    }

//    fun addFavoritePokemon(pokemon: Pokemon) {
//        viewModelScope.launch {
//            addFavoritePokemonUseCase.addFavoritePokemon(pokemon)
//        }
//    }

//    fun deleteFavoritePokemon(pokemon: Pokemon) {
//        viewModelScope.launch {
//            removeFavoritePokemonUseCase.removeFavoritePokemon(pokemon)
//        }
//    }
//
//    fun isPokemonFavorite(id: Int): Boolean {
//        return getIsPokemonFavoriteUseCase.isPokemonFavorite(id)
//    }

    fun getPokemonOfType(type : String) {
        cancelJobIfRunning()
        job = viewModelScope.launch(coroutineExceptionHandler) {
            _myTypePokemon.value = Result.Loading
            _myTypePokemon.value = Result.Success(repository.getAllPokemonOfType(type))
        }
    }

    fun getAllPokemonNames() {
        cancelJobIfRunning()
        job = viewModelScope.launch(coroutineExceptionHandler) {
            _myPokemonNamesList.value = Result.Loading
            _myPokemonNamesList.value =
                Result.Success(repository.getAllPokemonNames(Constants.TOTAL_POKEMONS))
        }
    }

    private fun checkIfContainsPokemon(pokemonList: MutableList<Pokemon>, pokemon: Pokemon): Boolean {
        pokemonList.forEach {
            if (it.id == pokemon.id) return true
        }
        return false
    }

    private fun cancelJobIfRunning() {
        if (job.isActive) {
            job.cancel()
        }
    }
}