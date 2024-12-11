package com.example.collectioncard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.collectioncard.model.PokemonDetails;
import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;
import com.example.collectioncard.ui.dashboard.DashboardFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PokemonDetailsActivity";
    private TextView numberTextView;
    private TextView typesTextView;
    private TextView abilitiesTextView;
    private TextView statsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        // In your PokemonDetailsActivity.java

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());


        // Find views
        TextView nameTextView = findViewById(R.id.pokemonName);
        numberTextView = findViewById(R.id.pokemonNumber);
        typesTextView = findViewById(R.id.pokemonTypes);
        abilitiesTextView = findViewById(R.id.pokemonAbilities);
        statsTextView = findViewById(R.id.pokemonStats);
        ImageView imageView = findViewById(R.id.pokemonImage);


        // Get data from Intent
        String name = getIntent().getStringExtra("pokemon_name");
        int number = getIntent().getIntExtra("pokemon_number", -1);

        if (name != null && number != -1) {
            nameTextView.setText(name);
            numberTextView.setText("#" + number);
            Glide.with(this)
                    .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + number + ".png")
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView);

            // Fetch details from API
            fetchPokemonDetails(number);
        } else {
            Log.e(TAG, "Failed to retrieve Pokémon details from Intent");
            Toast.makeText(this, "Invalid Pokémon data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchPokemonDetails(int number) {


        PokeApiService apiService = ApiClient.getRetrofitInstance().create(PokeApiService.class);
        Call<PokemonDetails> call = apiService.getPokemonDetails(String.valueOf(number));

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetails> call, @NonNull Response<PokemonDetails> response) {

                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails details = response.body();
                    Log.d(TAG, "Pokemon details: " + details);

                    // Update UI
                    updateUI(details);
                } else {
                    Log.e(TAG, "API response unsuccessful: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetails> call, @NonNull Throwable t) {

                Log.e(TAG, "API call failed: " + t.getMessage());
                Toast.makeText(PokemonDetailsActivity.this, "Failed to load Pokémon details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(PokemonDetails details) {
        // Update types
        StringBuilder typesBuilder = new StringBuilder();
        if (details.getTypes() != null) {
            for (PokemonDetails.Type type : details.getTypes()) {
                typesBuilder.append(type.getType().getName()).append(", ");
            }
            typesTextView.setText(typesBuilder.toString().replaceAll(", $", ""));
        } else {
            typesTextView.setText("No types available");
        }

        // Update abilities
        StringBuilder abilitiesBuilder = new StringBuilder();
        if (details.getAbilities() != null) {
            for (PokemonDetails.Ability ability : details.getAbilities()) {
                abilitiesBuilder.append(ability.getAbility().getName()).append(", ");
            }
            abilitiesTextView.setText(abilitiesBuilder.toString().replaceAll(", $", ""));
        } else {
            abilitiesTextView.setText("No abilities available");
        }

        // Update stats
        StringBuilder statsBuilder = new StringBuilder();
        if (details.getStats() != null) {
            for (PokemonDetails.Stat stat : details.getStats()) {
                statsBuilder.append(stat.getStat().getName()).append(": ").append(stat.getBaseStat()).append("\n");
            }
            statsTextView.setText(statsBuilder.toString());
        } else {
            statsTextView.setText("No stats available");
        }
    }
}
