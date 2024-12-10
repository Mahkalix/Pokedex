package com.example.collectioncard;

import android.content.Context;
import android.content.Intent;
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

    private final List<Pokemon> pokemonList;
    private final Context context; // Ajout du contexte pour gérer l'Intent

    // Constructeur
    public PokemonAdapter(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.context = context;
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

        // Mettre à jour le texte du nom
        holder.nameTextView.setText(pokemon.getName());

        // Charger l'image avec Glide
        Glide.with(holder.itemView.getContext())
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png")
                .placeholder(R.drawable.placeholder_image) // Image de remplacement
                .error(R.drawable.error_image)            // Image en cas d'erreur
                .into(holder.pokemonImageView);

        // Gérer le clic sur un élément
        holder.itemView.setOnClickListener(v -> {
            // Créer un Intent pour démarrer l'activité PokemonDetailsActivity
            Intent intent = new Intent(context, PokemonDetailsActivity.class);

            // Ajouter des données au Pokémon (nom et numéro)
            intent.putExtra("pokemon_name", pokemon.getName());
            intent.putExtra("pokemon_number", pokemon.getNumber());


            // Lancer l'activité
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView pokemonImageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonName);
            pokemonImageView = itemView.findViewById(R.id.pokemonImage);
        }
    }
}
