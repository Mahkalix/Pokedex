package com.example.collectioncard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.collectioncard.model.Pokemon;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private List<Pokemon> pokemonList;

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);
        return new PokemonViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.nameTextView.setText(pokemon.getName());

        // Utilise Glide pour charger l'image
        String imageUrl = pokemon.getImageUrl();  // Assure-toi d'avoir une URL valide

        Glide.with(holder.itemView.getContext())
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png")
                .placeholder(R.drawable.error_image)  // Optionnel : image de remplacement pendant le chargement
                .error(R.drawable.error_image)  // Optionnel : image en cas d'erreur
                .into(holder.pokemonImageView);
    }


    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView pokemonImageView;

        public PokemonViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonName);
            pokemonImageView = itemView.findViewById(R.id.pokemonImage);
        }
    }
}
