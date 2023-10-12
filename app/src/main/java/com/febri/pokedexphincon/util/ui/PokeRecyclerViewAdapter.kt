package com.febri.pokedexphincon.util.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febri.core_data.model.Pokemon
import com.febri.pokedexphincon.databinding.PokemonItemBinding
import com.febri.pokedexphincon.databinding.ShimmerProgressAnimationBinding

class PokeRecyclerViewAdapter(
    private val clickListener: (Pokemon) -> Unit,
    private val favoriteButtonClickListener: (Pokemon, Boolean) -> Unit,
    var showLoadingAnimation: Boolean,
    private val isPokemonFavorite: (Int) -> Boolean,
    private val lastPosition: Int?
) : ListAdapter<Pokemon, PokeRecyclerViewAdapter.PokemonViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view: View? = null
        if (viewType == 1) {
            val itemBinding = PokemonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PokemonViewHolder(itemBinding)
        } else if (viewType == 2 || viewType == 3) {
            val itemBinding =
                ShimmerProgressAnimationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PokemonViewHolder(itemBinding)
        }
        return PokemonViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        if (holder.itemViewType == 3) {
            holder.itemView.layoutParams.height = 0
            holder.itemView.visibility = View.GONE
        }
        if (holder.itemViewType == 1) {
            if (getItem(position) != null) holder.bind(getItem(position))
        }
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var pokemonItemBinding: PokemonItemBinding
        private lateinit var shimmerProgressAnimationBinding: ShimmerProgressAnimationBinding

        constructor(itemBinding: PokemonItemBinding) : this(itemBinding.root) {
            this.pokemonItemBinding = itemBinding
        }

        constructor(itemBinding: ShimmerProgressAnimationBinding) : this(itemBinding.root) {
            this.shimmerProgressAnimationBinding = itemBinding
        }

        private var dominantColor: Int = Color.GRAY

        fun bind(pokemon: Pokemon) {
            with(pokemonItemBinding) {
                if (pokemon.dominantColor != null) {
                    cardView.setCardBackgroundColor(pokemon.dominantColor!!)
                } else {
                    cardView.setCardBackgroundColor(dominantColor)
                }
                setPokemonImages(pokemon)

                pokemon.name = pokemon.name.replaceFirstChar { it.uppercase() }
                pokemonName.text = pokemon.name
                cardView.setOnClickListener {
                    pokemon.dominantColor = dominantColor
                    clickListener(pokemon)
                }
            }
        }

        private fun setPokemonImages(pokemon: Pokemon) {
            Glide.with(itemView.context.applicationContext)
                .asBitmap()
                .load(pokemon.sprites.other.officialArtwork.frontDefault)
                .centerCrop()
                .into(pokemonItemBinding.pokemonImage)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (lastPosition != null) {
            if (position == lastPosition) 3 else if (position == currentList.size) 2 else 1
        } else {
            if (position == currentList.size) 2 else 1
        }
    }

    override fun getItemCount(): Int {
        if (super.getItemCount() == 0) {
            return super.getItemCount()
        } else if (showLoadingAnimation) {
            return super.getItemCount() + 1
        }
        return super.getItemCount()
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem
        }
    }
}
