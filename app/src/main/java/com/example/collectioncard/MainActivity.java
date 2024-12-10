package com.example.collectioncard;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collectioncard.databinding.ActivityMainBinding;
import com.example.collectioncard.model.Pokemon;
import com.example.collectioncard.model.PokemonDetails;
import com.example.collectioncard.model.PokemonResponse;
import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PokemonAdapter pokemonAdapter;
    private List<Pokemon> pokemonList;

    public MainActivity() {
        pokemonList = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configurer Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Utilisation de View Binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurer la RecyclerView
        recyclerView = binding.recyclerViewPokemons;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pokemonAdapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(pokemonAdapter);

        // Appel à l'API pour récupérer les Pokémon
        fetchPokemonData(retrofit);

        // Configurer le BottomNavigationView
        BottomNavigationView navView = binding.navView;
        navView.setItemIconTintList(null);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(navView, navController);

        // Cacher ou afficher la barre de navigation pour certaines destinations
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home) {
                navView.setVisibility(View.GONE);
            } else {
                navView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fetchPokemonData(Retrofit retrofit) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);
        Call<PokemonResponse> call = apiService.getPokemons();

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList = response.body().getResults();

                    // Récupérer les détails pour chaque Pokémon
                    for (Pokemon pokemon : pokemonList) {
                        fetchPokemonDetails(retrofit, pokemon);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Erreur lors du chargement des Pokémon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Échec de la récupération des données", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    private void fetchPokemonDetails(Retrofit retrofit, Pokemon pokemon) {
        PokeApiService apiService = retrofit.create(PokeApiService.class);
        Call<PokemonDetails> call = apiService.getPokemonDetails(pokemon.getUrl());

        call.enqueue(new Callback<PokemonDetails>() {
            @Override
            public void onResponse(Call<PokemonDetails> call, Response<PokemonDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PokemonDetails pokemonDetails = response.body();
                    pokemon.setImageUrl(pokemonDetails.getSprites().getFrontDefault());

                    // Notifier l'adaptateur pour mettre à jour la RecyclerView
                    pokemonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PokemonDetails> call, Throwable t) {
                Log.e("API_ERROR", "Erreur lors du chargement des détails du Pokémon : " + t.getMessage());
            }
        });
    }
}
