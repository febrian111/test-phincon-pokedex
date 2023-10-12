package com.febri.pokedexphincon.home

import androidx.lifecycle.ViewModel
import com.febri.core_data.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModelTwo @Inject constructor(repository: PokedexRepository) : ViewModel() {
}