package com.febri.pokedexphincon.home


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febri.core_data.model.Pokemon
import com.febri.core_data.util.Constants
import com.febri.core_data.util.Result
import com.febri.pokedexphincon.R
import com.febri.pokedexphincon.databinding.FragmentHomeBinding
import com.febri.pokedexphincon.util.ui.PokeRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private lateinit var currentType: String
    var pokemonsDisplayed: Int = 0
    var loading: Boolean = false
    var failureFlag = false
    var allTypesList: MutableList<Pokemon> = mutableListOf()
    var toLoadList: MutableList<Pokemon> = mutableListOf()
    var pagingFlag: Boolean = true

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: PokeRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupViews()
        setupObservers()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        allTypesList.clear()
        _binding = null
    }

    private fun setupViews() {
        with(binding) {
            setupAdapter(allTypesList.size)
            showLoadingAnimation()
            customToolbarTextview.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.title_color
                )
            )
        }
    }

    private fun setupObservers() {
        viewModel.myPokemon.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Failure -> onLoadingFailure()
                Result.Loading -> loading = true
                is Result.Success -> onLoadingSuccess(result.value, adapter)
            }
        }
        viewModel.myTypePokemon.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success && allTypesList.isEmpty()) {
                toLoadList.clear()
                result.value.results.forEach {
                    val trimmedUrl = it.pokemon.url?.dropLast(1)
                    it.pokemon.id = trimmedUrl!!.substring(trimmedUrl.lastIndexOf("/") + 1).toInt()
                    if (it.pokemon.id <= Constants.TOTAL_POKEMONS) allTypesList.add(it.pokemon)
                }
                setupAdapter(allTypesList.size)
                for (i in 0 until Constants.POKEMONS_LOAD_LIMIT) {
                    toLoadList.add(result.value.results[i].pokemon)
                }
                viewModel.getPokemon(toLoadList)
            }
        }
        viewModel.myPokemonNamesList.observe(viewLifecycleOwner) { result ->
            if (result is Result.Success && allTypesList.isEmpty()) {
                toLoadList.clear()
                result.value.results.forEach {
                    val trimmedUrl = it.url?.dropLast(1)
                    it.id = trimmedUrl!!.substring(trimmedUrl.lastIndexOf("/") + 1).toInt()
                    if (it.id <= Constants.TOTAL_POKEMONS)
                        allTypesList.add(it)
                }
                setupAdapter(allTypesList.size)
                for (i in 0 until Constants.POKEMONS_LOAD_LIMIT) {
                    toLoadList.add(result.value.results[i])
                }
                viewModel.getPokemon(toLoadList)
            }
        }
    }

    private fun onLoadingSuccess(
        pokemonList: MutableList<Pokemon>,
        adapter: PokeRecyclerViewAdapter
    ) {
        with(binding) {
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
            noInternetLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
//        verifyPokemonListType(pokemonList)
        adapter.submitList(pokemonList.toMutableList())
        pokemonsDisplayed = pokemonList.size
        loading = false
    }

    private fun onLoadingFailure() {
        with(binding) {
            if (pokemonsDisplayed == 0) noInternetLayout.visibility = View.VISIBLE
            else Toast.makeText(
                context,
                resources.getString(R.string.check_internet_connection),
                Toast.LENGTH_SHORT
            )
                .show()
            shimmerLayout.stopShimmer()
            shimmerLayout.visibility = View.GONE
        }
        failureFlag = true
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        onConnectionRestored()
                    }
                })
            }
        }
    }

    private fun setupAdapter(lastPosition: Int) {
        adapter = PokeRecyclerViewAdapter(clickListener = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it)
            Navigation.findNavController(requireView()).navigate(action)
        }, favoriteButtonClickListener = { pokemon: Pokemon, isSelected: Boolean ->
//            if (isSelected) {
//                viewModel.deleteFavoritePokemon(pokemon)
//            } else {
//                viewModel.addFavoritePokemon(pokemon)
//            }
        }, true, isPokemonFavorite = {
//            return@PokeRecyclerViewAdapter viewModel.isPokemonFavorite(it)
            return@PokeRecyclerViewAdapter false
        }, lastPosition)
        setupRecycler(adapter)
    }

    private fun setupRecycler(adapter: PokeRecyclerViewAdapter) {
        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
            recyclerView.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.bg_color
                )
            )
            recyclerView.hasFixedSize()
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (pagingFlag) callGetPokemon()
                }
            })
        }
    }

    private fun showLoadingAnimation() {
        if (pokemonsDisplayed == 0) {
            binding.shimmerLayout.startShimmer()
            binding.shimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun callGetPokemon() {
        if (!binding.recyclerView.canScrollVertically(1) && !loading && pokemonsDisplayed >= Constants.POKEMONS_LOAD_LIMIT) {
            toLoadList.clear()
            if (allTypesList.size - pokemonsDisplayed >= 20) {
                for (i in pokemonsDisplayed until pokemonsDisplayed + Constants.POKEMONS_LOAD_LIMIT) {
                    toLoadList.add(allTypesList[i])
                }
                pokemonsDisplayed += Constants.POKEMONS_LOAD_LIMIT
            } else if (pokemonsDisplayed < allTypesList.size) {
                for (i in pokemonsDisplayed until allTypesList.size) {
                    toLoadList.add(allTypesList[i])
                }
                pokemonsDisplayed = allTypesList.size
            }
            viewModel.getPokemon(toLoadList)
        }
    }

    private fun onConnectionRestored() {
        if (failureFlag) {
            activity?.runOnUiThread { binding.noInternetLayout.visibility = View.GONE }
            if (toLoadList.isNotEmpty()) {
                viewModel.getPokemon(toLoadList)
                activity?.runOnUiThread { showLoadingAnimation() }
                failureFlag = false
            } else {
                activity?.runOnUiThread {
                    pokemonsDisplayed = 0
                    viewModel.list.clear()
                    viewModel.getAllPokemonNames()
                    showLoadingAnimation()
                    binding.recyclerView.visibility = View.INVISIBLE
                }
            }
        }
    }
}

//    private fun verifyPokemonListType(pokemonList: MutableList<Pokemon>) {
//        if (currentType != resources.getString(R.string.all_types).lowercase()) {
//            val type = Type(TypeX(currentType))
//            pokemonList.forEach {
//                if (!it.types.contains(type)) {
//                    pokemonList.remove(it)
//                }
//            }
//        }
//    }
//}