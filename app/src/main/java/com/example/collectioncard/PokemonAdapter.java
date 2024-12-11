package com.example.collectioncard;

import android.content.Context;
import android.content.Intent;
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
import com.example.collectioncard.model.PokemonDetails;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {

    private static final String TAG = "PokemonAdapter";
    private final List<Pokemon> pokemonList; // Liste des Pokémon à afficher
    private final Context context; // Contexte pour gérer l'Intent

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

        // Construire un objet PokemonDetails à partir des données disponibles
        PokemonDetails pokemonDetails = new PokemonDetails();
        pokemon.setName(pokemon.getName());
        pokemonDetails.setSprites(new PokemonDetails.Sprites());
        pokemonDetails.getSprites().setFrontDefault(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png"
        );

        // Mettre à jour l'affichage dans l'item
        holder.nameTextView.setText(pokemon.getName());
        Glide.with(holder.itemView.getContext())
                .load(pokemonDetails.getSprites().getFrontDefault())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.pokemonImageView);

        // Gérer le clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PokemonDetailsActivity.class);

            // Passer les données du Pokémon via Intent
            intent.putExtra("pokemon_name", pokemon.getName());
            intent.putExtra("pokemon_number", pokemon.getNumber());

            // Convertir et passer les types du Pokémon
            if (pokemonDetails.getTypes() != null) {
                StringBuilder types = new StringBuilder();
                for (PokemonDetails.Type type : pokemonDetails.getTypes()) {
                    types.append(type.getType().getName()).append(", ");
                }
                intent.putExtra("pokemon_types", types.toString().trim());
            }

            // Ajouter les abilities
            if (pokemonDetails.getAbilities() != null) {
                StringBuilder abilities = new StringBuilder();
                for (PokemonDetails.Ability ability : pokemonDetails.getAbilities()) {
                    abilities.append(ability.getAbility().getName()).append(", ");
                }
                intent.putExtra("pokemon_abilities", abilities.toString().trim());
            }

            // Ajouter les stats
            if (pokemonDetails.getStats() != null) {
                StringBuilder stats = new StringBuilder();
                for (PokemonDetails.Stat stat : pokemonDetails.getStats()) {
                    stats.append(stat.getStat().getName())
                            .append(": ")
                            .append(stat.getBaseStat())
                            .append("\n");
                }
                intent.putExtra("pokemon_stats", stats.toString().trim());
            }

            Log.d(TAG, "Sending Pokémon Details: " + pokemonDetails.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // Classe interne pour gérer les vues de chaque élément
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView pokemonImageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonName); // Correspond à l'ID dans le layout item_pokemon.xml
            pokemonImageView = itemView.findViewById(R.id.pokemonImage); // ID pour l'image
        }
    }
}
