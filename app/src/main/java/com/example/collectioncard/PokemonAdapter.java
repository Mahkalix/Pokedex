package com.example.collectioncard;

import android.content.Context;
import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> implements Filterable {
    private final List<Pokemon> pokemonList;
    private final List<Pokemon> allPokemon;
    private final Runnable onResultsChanged;

    public PokemonAdapter(List<Pokemon> pokemon, Context context) {
        this(pokemon, () -> {});
    }

    public PokemonAdapter(List<Pokemon> pokemon, Runnable onResultsChanged) {
        this.pokemonList = new ArrayList<>(pokemon);
        this.allPokemon = new ArrayList<>(pokemon);
        this.onResultsChanged = onResultsChanged;
        setHasStableIds(true);
    }

    @Override public long getItemId(int position) { return pokemonList.get(position).getNumber(); }

    @NonNull @Override public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PokemonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
        Pokemon pokemon = pokemonList.get(position);
        holder.name.setText(pokemon.getName());
        holder.itemView.setContentDescription(pokemon.getName() + ", numéro " + pokemon.getNumber());
        Glide.with(holder.itemView)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/" + pokemon.getNumber() + ".png")
                .placeholder(R.drawable.placeholder_image).error(R.drawable.error_image).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PokemonDetailsActivity.class);
            intent.putExtra("pokemon_name", pokemon.getName());
            intent.putExtra("pokemon_number", pokemon.getNumber());
            v.getContext().startActivity(intent);
        });
    }

    @Override public int getItemCount() { return pokemonList.size(); }
    @Override public Filter getFilter() { return filter; }

    private final Filter filter = new Filter() {
        @Override protected FilterResults performFiltering(CharSequence constraint) {
            List<Pokemon> matches = new ArrayList<>();
            for (Pokemon pokemon : allPokemon) if (pokemon.matches(constraint == null ? "" : constraint.toString())) matches.add(pokemon);
            FilterResults result = new FilterResults();
            result.values = matches;
            result.count = matches.size();
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override protected void publishResults(CharSequence constraint, FilterResults results) {
            pokemonList.clear();
            pokemonList.addAll((List<Pokemon>) results.values);
            notifyDataSetChanged();
            onResultsChanged.run();
        }
    };

    static class PokemonViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final ImageView image;
        PokemonViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.pokemonName);
            image = view.findViewById(R.id.pokemonImage);
        }
    }
}
