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
    private TextView typeTextView;

    private TextView numberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        // Find views
        TextView nameTextView = findViewById(R.id.pokemonName);
        numberTextView = findViewById(R.id.pokemonNumber);
        typesTextView = findViewById(R.id.pokemonTypes);
        abilitiesTextView = findViewById(R.id.pokemonAbilities);
        statsTextView = findViewById(R.id.pokemonStats);
        ImageView imageView = findViewById(R.id.pokemonImage);
        typeTextView = findViewById(R.id.pokemonType);

        // Get data from Intent
        String name = getIntent().getStringExtra("pokemon_name");
        int number = getIntent().getIntExtra("pokemon_number", -1);

        // Update name and image
        if (name != null && number != -1) {
            nameTextView.setText(name);
            Glide.with(this)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + number + ".png")
                    .into(imageView);
            // Vérifier l'URL avant de faire la requête
            String url = "https://pokeapi.co/api/v2/pokemon/" + number;
            Log.d(TAG, "number " + number);

            //ajouter le numero du pokemon dans le textview
            numberTextView.setText("#" + number);

        } else {
            Log.e(TAG, "Failed to retrieve Pokemon details from Intent");
        }
    }

    private void fetchPokemonDetails(int number) {
        // Instantiate Retrofit client
        PokeApiService apiService = ApiClient.getRetrofitInstance().create(PokeApiService.class);

        // Make API request to get Pokemon details
        Call<PokemonDetails> call = apiService.getPokemonDetails(String.valueOf(number));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetails> call, @NonNull Response<PokemonDetails> response) {
                Log.d(TAG, "Response code: " + response.code()); // Log du code de statut
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails details = response.body();

                    // Log the details for debugging
                    Log.d(TAG, "Pokemon details: " + details);

                    // Update types
                    StringBuilder typesBuilder = new StringBuilder();
                    for (PokemonDetails.Type type : details.getTypes()) {
                        typesBuilder.append(type.getType().getName()).append(", ");
                    }
                    typesTextView.setText(typesBuilder.toString().replaceAll(", $", ""));

                    // Update abilities
                    StringBuilder abilitiesBuilder = new StringBuilder();
                    for (PokemonDetails.Ability ability : details.getAbilities()) {
                        abilitiesBuilder.append(ability.getAbility().getName()).append(", ");
                    }
                    abilitiesTextView.setText(abilitiesBuilder.toString().replaceAll(", $", ""));

                    // Update stats
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