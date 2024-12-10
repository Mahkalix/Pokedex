package com.example.collectioncard;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.collectioncard.model.PokemonDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;

public class PokemonDetailsActivity extends AppCompatActivity {

    private static final String TAG = "INFOS";

    private TextView typesTextView;
    private TextView abilitiesTextView;
    private TextView statsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        // Récupère les données passées dans l'Intent
        String name = getIntent().getStringExtra("pokemon_name");
        int number = getIntent().getIntExtra("pokemon_number", -1);

        // Trouve les vues
        TextView nameTextView = findViewById(R.id.pokemonName);
        typesTextView = findViewById(R.id.pokemonTypes);
        abilitiesTextView = findViewById(R.id.pokemonAbilities);
        statsTextView = findViewById(R.id.pokemonStats);
        ImageView imageView = findViewById(R.id.pokemonImage);

        // Met à jour le nom et l'image
        if (name != null && number != -1) {
            nameTextView.setText(name);
            Glide.with(this)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + number + ".png")
                    .into(imageView);

            // Appelle l'API pour récupérer les détails
            fetchPokemonDetails(number);
        } else {
            Log.e(TAG, "Failed to retrieve Pokemon details from Intent");
        }
    }

    private void fetchPokemonDetails(int number) {
        // Instancie le client Retrofit
        PokeApiService apiService = ApiClient.getRetrofitInstance().create(PokeApiService.class);

        // Effectue la requête pour obtenir les détails du Pokémon
        Call<PokemonDetails> call = apiService.getPokemonDetails(String.valueOf(number));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetails> call, @NonNull Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails details = response.body();

                    // Met à jour les types
                    StringBuilder typesBuilder = new StringBuilder();
                    for (PokemonDetails.Type type : details.getTypes()) {
                        typesBuilder.append(type.getType().getName()).append(", ");
                    }
                    typesTextView.setText(typesBuilder.toString().replaceAll(", $", ""));

                    // Met à jour les capacités
                    StringBuilder abilitiesBuilder = new StringBuilder();
                    for (PokemonDetails.Ability ability : details.getAbilities()) {
                        abilitiesBuilder.append(ability.getAbility().getName()).append(", ");
                    }
                    abilitiesTextView.setText(abilitiesBuilder.toString().replaceAll(", $", ""));

                    // Met à jour les statistiques
                    StringBuilder statsBuilder = new StringBuilder();
                    for (PokemonDetails.Stat stat : details.getStats()) {
                        statsBuilder.append(stat.getStat().getName()).append(": ")
                                .append(stat.getBaseStat()).append("\n");
                    }
                    statsTextView.setText(statsBuilder.toString());
                } else {
                    Log.e(TAG, "Failed to fetch Pokemon details from API");
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetails> call, @NonNull Throwable t) {
                Log.e(TAG, "API call failed: " + t.getMessage());
            }
        });
    }
}
