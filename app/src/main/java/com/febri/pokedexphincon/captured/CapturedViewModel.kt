package com.febri.pokedexphincon.captured

import androidx.lifecycle.ViewModel
import com.febri.core_data.PokedexRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CapturedViewModel @Inject constructor(private val repository: PokedexRepository): ViewModel() {

}
