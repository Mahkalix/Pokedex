package com.example.collectioncard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.collectioncard.model.Pokemon;
import com.example.collectioncard.model.PokemonDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> implements Filterable {

    private static final String TAG = "PokemonAdapter";
    private final List<Pokemon> pokemonList; // List of Pokémon to display
    private final List<Pokemon> pokemonListFull; // Full list of Pokémon for filtering
    private final Context context; // Context to handle Intent

    // Constructor
    public PokemonAdapter(List<Pokemon> pokemonList, Context context) {
        this.pokemonList = pokemonList;
        this.pokemonListFull = new ArrayList<>(pokemonList); // Initialize the full list
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

        // Build a PokemonDetails object from available data
        PokemonDetails pokemonDetails = new PokemonDetails();
        pokemon.setName(pokemon.getName());
        pokemonDetails.setSprites(new PokemonDetails.Sprites());
        pokemonDetails.getSprites().setFrontDefault(
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + pokemon.getNumber() + ".png"
        );

        Log.d(TAG, "onBindViewHolder: " + pokemonDetails.getSprites().getFrontDefault());
        Log.d(TAG, "Generated Sprite URL: " + pokemonDetails.getSprites().getFrontDefault());

        // Update the item view
        holder.nameTextView.setText(pokemon.getName());
        Glide.with(holder.itemView.getContext())
                .load(pokemonDetails.getSprites().getFrontDefault())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.pokemonImageView);

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PokemonDetailsActivity.class);

            // Pass Pokémon data via Intent
            intent.putExtra("pokemon_name", pokemon.getName());
            intent.putExtra("pokemon_number", pokemon.getNumber());

            // Convert and pass Pokémon types
            if (pokemonDetails.getTypes() != null) {
                StringBuilder types = new StringBuilder();
                for (PokemonDetails.Type type : pokemonDetails.getTypes()) {
                    types.append(type.getType().getName()).append(", ");
                }
                intent.putExtra("pokemon_types", types.toString().trim());
            }

            // Add abilities
            if (pokemonDetails.getAbilities() != null) {
                StringBuilder abilities = new StringBuilder();
                for (PokemonDetails.Ability ability : pokemonDetails.getAbilities()) {
                    abilities.append(ability.getAbility().getName()).append(", ");
                }
                intent.putExtra("pokemon_abilities", abilities.toString().trim());
            }

            // Add stats
            if (pokemonDetails.getStats() != null) {
                StringBuilder stats = new StringBuilder();
                for (PokemonDetails.Stat stat : pokemonDetails.getStats()) {
                    Log.d("lol", "Stat Name: " + stat.getStat().getName());
                    Log.d("lol", "Base Stat: " + stat.getBaseStat());
                    Log.d("lol", "Effort: " + stat.getEffort());
                    stats.append(stat.getStat().getName())
                            .append(": ")
                            .append(stat.getBaseStat())
                            .append(" (Effort: ")
                            .append(getEffortStars(stat.getEffort()))  // Add stars for effort
                            .append(")\n");
                }
                intent.putExtra("pokemon_stats", stats.toString().trim());
            } else {
                intent.putExtra("pokemon_stats", "No stats available");
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    @Override
    public Filter getFilter() {
        return pokemonFilter;
    }

    private final Filter pokemonFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pokemon> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(pokemonListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Pokemon item : pokemonListFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pokemonList.clear();
            pokemonList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    // Inner class to handle views of each item
    public static class PokemonViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView pokemonImageView;

        public PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pokemonName); // Corresponds to the ID in item_pokemon.xml
            pokemonImageView = itemView.findViewById(R.id.pokemonImage); // ID for the image
        }
    }

    // Method to generate stars based on effort
    private String getEffortStars(int effort) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < effort; i++) {
            stars.append("*");
        }
        return stars.toString();
    }
}