package com.example.collectioncard;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.collectioncard.model.PokemonDetails;
import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetailsActivity extends AppCompatActivity {

    private static final String TAG = "PokemonDetailsActivity";
    private TextView numberTextView;
    private TextView typesTextView;
    private TextView abilitiesTextView;
    private TextView statsTextView;

    private TextView heightTextView;
    private TextView weightTextView;
    private ProgressBar progressBarHP;
    private ProgressBar progressBarAttack;
    private ProgressBar progressBarDefense;
    private ProgressBar progressBarSpecialAttack;
    private ProgressBar progressBarSpecialDefense;
    private ProgressBar progressBarSpeed;

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
        heightTextView = findViewById(R.id.pokemonHeight);
        weightTextView = findViewById(R.id.pokemonWeight);
        ImageView imageView = findViewById(R.id.pokemonImage);
        ImageView backButton = findViewById(R.id.backButton);
        progressBarHP = findViewById(R.id.progressBarHP);
        progressBarAttack = findViewById(R.id.progressBarAttack);
        progressBarDefense = findViewById(R.id.progressBarDefense);
        progressBarSpecialAttack = findViewById(R.id.progressBarSpecialAttack);
        progressBarSpecialDefense = findViewById(R.id.progressBarSpecialDefense);
        progressBarSpeed = findViewById(R.id.progressBarSpeed);

        if (nameTextView == null || numberTextView == null || typesTextView == null ||
                abilitiesTextView == null || statsTextView == null || imageView == null || backButton == null) {
            Log.e(TAG, "One or more views are null");
            return;
        }

        backButton.setOnClickListener(v -> onBackPressed());

        // Get data from Intent
        String name = getIntent().getStringExtra("pokemon_name");
        int number = getIntent().getIntExtra("pokemon_number", -1);
        String types = getIntent().getStringExtra("pokemon_types");
        String abilities = getIntent().getStringExtra("pokemon_abilities");
        String stats = getIntent().getStringExtra("pokemon_stats");

        // Validate data
        types = types != null ? types : "No types available";
        abilities = abilities != null ? abilities : "No abilities available";
        stats = stats != null ? stats : "No stats available";

        if (name != null && number != -1) {
            nameTextView.setText(name);
            numberTextView.setText("N°" + number);
            typesTextView.setText(types);
            abilitiesTextView.setText(abilities);
            statsTextView.setText(stats);

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
        String pokemonUrl = "https://pokeapi.co/api/v2/pokemon/" + number;

        PokeApiService apiService = ApiClient.getRetrofitInstance().create(PokeApiService.class);
        Call<PokemonDetails> call = apiService.getPokemonDetails(pokemonUrl);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetails> call, @NonNull Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails details = response.body();
                    Log.d(TAG, "Pokemon details: " + details);

                    // Log stats to check data
                    if (details.getStats() != null && !details.getStats().isEmpty()) {
                        for (PokemonDetails.Stat stat : details.getStats()) {
                            if (stat != null && stat.getStat() != null && stat.getStat().getName() != null) {
                                Log.d(TAG, "Stat name: " + stat.getStat().getName());
                                Log.d(TAG, "Stat base: " + stat.getBaseStat());
                                Log.d(TAG, "Stat effort: " + stat.getEffort());
                            }
                        }
                    } else {
                        Log.d(TAG, "No stats available");
                    }

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
        if (details == null) {
            Log.e(TAG, "Pokemon details are null");
            return;
        }

        // Update types with emojis
        if (details.getTypes() != null && !details.getTypes().isEmpty()) {
            StringBuilder typesBuilder = new StringBuilder();
            for (PokemonDetails.Type type : details.getTypes()) {
                if (type != null && type.getType() != null && type.getType().getName() != null) {
                    typesBuilder.append(getTypeEmoji(type.getType().getName())).append(" ").append(type.getType().getName()).append(", ");
                }
            }
            typesTextView.setText(typesBuilder.length() > 0 ? typesBuilder.toString().replaceAll(", $", "") : "No types available");
        } else {
            typesTextView.setText("No types available");
        }

        // Update abilities
        if (details.getAbilities() != null && !details.getAbilities().isEmpty()) {
            StringBuilder abilitiesBuilder = new StringBuilder();
            for (PokemonDetails.Ability ability : details.getAbilities()) {
                if (ability != null && ability.getAbility() != null && ability.getAbility().getName() != null) {
                    abilitiesBuilder.append(ability.getAbility().getName()).append(", ");
                }
            }
            abilitiesTextView.setText(abilitiesBuilder.length() > 0 ? abilitiesBuilder.toString().replaceAll(", $", "") : "No abilities available");
        } else {
            abilitiesTextView.setText("No abilities available");
        }

        // Update height, weight
        heightTextView.setText("🧍" + details.getHeight() / 10.0 + "m");
        weightTextView.setText("⚖️" + details.getWeight() / 10.0 + "kg");

        // Update stats and progress bars
        if (details.getStats() != null && !details.getStats().isEmpty()) {
            StringBuilder statsBuilder = new StringBuilder();
            for (PokemonDetails.Stat stat : details.getStats()) {
                if (stat != null && stat.getStat() != null && stat.getStat().getName() != null) {
                    statsBuilder.append(stat.getStat().getName()).append(": ").append(stat.getBaseStat()).append("\n");
                    int baseStat = stat.getBaseStat();
                    int percentage = (baseStat * 100) / 255;
                    int percentageHp = (baseStat * 100) / 250;
                    int percentageAttack = (baseStat * 100) / 134;
                    int percentageDefense = (baseStat * 100) / 180;
                    int percentageSpeed = (baseStat * 100) / 140;
                    int percentageSpecialAttack = (baseStat * 100) / 154;
                    int percentageSpecialDefense = (baseStat * 100) / 105;

                    statsBuilder.append(stat.getStat().getName()).append(": ").append(baseStat).append(" (").append(percentage).append("%)").append("\n");

                    switch (stat.getStat().getName()) {
                        case "hp":
                            animateProgressBar(progressBarHP, percentageHp);
                            ((TextView) findViewById(R.id.hpTitle)).setText("❤️ "+"HP: " + baseStat);
                            break;
                        case "attack":
                            animateProgressBar(progressBarAttack, percentageAttack);
                            ((TextView) findViewById(R.id.attackTitle)).setText("💥 " +"Attack: " + baseStat);
                            break;
                        case "defense":
                            animateProgressBar(progressBarDefense, percentageDefense);
                            ((TextView) findViewById(R.id.defenseTitle)).setText("🛡️ "+"Defense: " + baseStat);
                            break;
                        case "special-attack":
                            animateProgressBar(progressBarSpecialAttack, percentageSpecialAttack);
                            ((TextView) findViewById(R.id.specialAttackTitle)).setText("🌟 "+"Special Attack: " + baseStat);
                            break;
                        case "special-defense":
                            animateProgressBar(progressBarSpecialDefense, percentageSpecialDefense);
                            ((TextView) findViewById(R.id.specialDefenseTitle)).setText("🧠 " +"Special Defense: " + baseStat);
                            break;
                        case "speed":
                            animateProgressBar(progressBarSpeed, percentageSpeed);
                            ((TextView) findViewById(R.id.speedTitle)).setText("🚀 "+"Speed: " + baseStat);
                            break;
                    }
                }
            }
            statsTextView.setText(statsBuilder.toString());
        } else {
            statsTextView.setText("No stats available");
        }
    }

    // Method to animate progress bars
    private void animateProgressBar(ProgressBar progressBar, int toValue) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, toValue);
        animation.setDuration(1000); // 1 second
        animation.start();
    }

    // Method to get the emoji for a Pokémon type
    private String getTypeEmoji(String type) {
        switch (type.toLowerCase()) {
            case "fire": return "🔥";
            case "water": return "💧";
            case "grass": return "🌿";
            case "electric": return "⚡";
            case "bug": return "🐛";
            case "poison": return "☠️";
            case "fighting": return "👊";
            case "normal": return "⚪";
            case "ghost": return "👻";
            case "psychic": return "🧠";
            case "dragon": return "🐉";
            case "fairy": return "🧚‍♀️";
            case "dark": return "🌑";
            case "ice": return "❄️";
            case "steel": return "🛠️";
            case "rock": return "🪨";
            case "ground": return "🌍";
            case "flying": return "🦅";
            default: return "❓"; // Default emoji for unknown types
        }
    }
}
