package com.example.collectioncard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collectioncard.authentification.LoginActivity;
import com.example.collectioncard.databinding.ActivityMainBinding;
import com.example.collectioncard.model.Pokemon;
import com.example.collectioncard.model.PokemonDetails;
import com.example.collectioncard.model.PokemonResponse;
import com.example.collectioncard.network.PokeApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private PokemonAdapter pokemonAdapter;
    private List<Pokemon> pokemonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        pokemonList = new ArrayList<>();

        RecyclerView recyclerView = binding.recyclerViewPokemons;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        pokemonAdapter = new PokemonAdapter(pokemonList, this);
        recyclerView.setAdapter(pokemonAdapter);

        fetchPokemonData(retrofit);

        BottomNavigationView navView = binding.navView;
        navView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });

        // Check if we need to navigate to the DashboardFragment
        if (getIntent().getBooleanExtra("navigateToDashboard", false)) {
            navController.navigate(R.id.navigation_dashboard);
        }
    }

    private void fetchPokemonData(Retrofit retrofit) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);
        Call<PokemonResponse> call = apiService.getPokemons();

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(@NonNull Call<PokemonResponse> call, @NonNull Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList.addAll(response.body().getResults());

                    for (Pokemon pokemon : pokemonList) {
                        fetchPokemonDetails(retrofit, pokemon);
                    }
                } else {
                   Toast.makeText(MainActivity.this, "Error loading Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable t) {
              Toast.makeText(MainActivity.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", Objects.requireNonNull(t.getMessage()));
            }
        });
    }

    private void fetchPokemonDetails(Retrofit retrofit, Pokemon pokemon) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);
        Call<PokemonDetails> call = apiService.getPokemonDetails(pokemon.getUrl());

        call.enqueue(new Callback<PokemonDetails>() {
            @Override
            public void onResponse(@NonNull Call<PokemonDetails> call, @NonNull Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails pokemonDetails = response.body();
                    pokemon.setImageUrl(pokemonDetails.getSprites().getFrontDefault());

                    pokemonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PokemonDetails> call, @NonNull Throwable t) {
                Log.e("API_ERROR", "Error loading Pokémon details: " + t.getMessage());Log.e("API_ERROR", "Erreur lors du chargement des détails du Pokémon : " + t.getMessage());
            }
        });
    }
}