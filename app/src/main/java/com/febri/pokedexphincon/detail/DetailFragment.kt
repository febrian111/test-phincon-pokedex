package com.febri.pokedexphincon.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.febri.core_data.model.Pokemon
import com.febri.pokedexphincon.databinding.FragmentDetailBinding
import com.febri.pokedexphincon.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel by viewModels<DetailViewModel>()
    private var dominantColor = Color.GRAY

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)?.supportActionBar?.setShowHideAnimationEnabled(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        arguments?.let { bundle ->
            setData(DetailFragmentArgs.fromBundle(bundle).pokemon)
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        activity?.window?.statusBarColor = dominantColor
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
            View.GONE
        activity?.window?.navigationBarColor = dominantColor
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bg_color)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)?.visibility =
            View.VISIBLE
        activity?.window?.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.bg_color)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setData(pokemon: Pokemon) {
        with(binding) {
//            pokeId.text = requireContext().getString(R.string.pokemon_number_format, pokemon.id)
            pokeName.text = pokemon.name
            pokeGenera.text = pokemon.genera
            pokeInfoDesc.text = pokemon.description
            pokeCaptureRate.text = pokemon.captureRate.toString()
            pokeXp.text = pokemon.baseExperience.toString()
            pokeHeight.text = getString(R.string.height_format, (pokemon.height.times(10)))
            pokeWeight.text = getString(R.string.weight_format, (pokemon.weight.div(10.0)))
            pokeHp.text = pokemon.stats[0].baseStat.toString()
            pokeAttack.text = pokemon.stats[1].baseStat.toString()
            pokeDefense.text = pokemon.stats[2].baseStat.toString()
            pokeSpecialAttack.text = pokemon.stats[3].baseStat.toString()
            pokeSpecialDefense.text = pokemon.stats[4].baseStat.toString()
            pokeSpeed.text = pokemon.stats[5].baseStat.toString()
//            pokeInfoTypeOne.text = pokemon.types[0].type.name
            pokeBack.setOnClickListener { activity?.onBackPressed() }
//            setPokemonTypes(pokemon.types)
//            if (viewModel.isPokemonFavorite(pokemon.id)) setFavoriteIcon(pokeFav)
            loadImage(pokeInfoImage, pokemon.sprites.other.officialArtwork.frontDefault)
//            pokeFav.setOnClickListener { favoriteButtonClickListener(pokeFav, pokemon) }
            dominantColor = pokemon.dominantColor!!
            pokeScrollView.setBackgroundColor(dominantColor)
            activity?.window?.statusBarColor = dominantColor
        }
    }


    private fun loadImage(pokeInfoImage: ImageView, imageUrl: String) {
        Glide.with(requireContext().applicationContext)
            .asBitmap()
            .load(imageUrl)
            .centerCrop()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(pokeInfoImage)
    }
}