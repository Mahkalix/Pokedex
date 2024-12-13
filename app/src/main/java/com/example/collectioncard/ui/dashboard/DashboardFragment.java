package com.example.collectioncard.ui.dashboard;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.collectioncard.PokemonAdapter;
import com.example.collectioncard.R;
import com.example.collectioncard.model.Pokemon;
import com.example.collectioncard.model.PokemonResponse;
import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private PokemonAdapter pokemonAdapter;
    private List<Pokemon> pokemonList;
    private ImageView loadingImage;
    private SearchView searchView;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Initialize the loading image
        loadingImage = rootView.findViewById(R.id.loadingImage);

        // Configure the RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewPokemonsDashboard);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Initialize the SearchView
        searchView = rootView.findViewById(R.id.searchViewPokemon);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (pokemonAdapter != null) {
                    pokemonAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        // Fetch Pokémon data
        fetchPokemonData();

        return rootView;
    }

    private void onLoading(boolean isLoading) {
        if (isLoading) {
            loadingImage.setVisibility(View.VISIBLE);
        } else {
            loadingImage.setVisibility(View.GONE);
        }
    }

    private void fetchPokemonData() {
        onLoading(true);
        PokeApiService service = ApiClient.getRetrofitInstance().create(PokeApiService.class);
        Call<PokemonResponse> call = service.getPokemons();

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                onLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList = response.body().getResults();

                    pokemonAdapter = new PokemonAdapter(pokemonList, getContext());
                    recyclerView.setAdapter(pokemonAdapter);
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                onLoading(false);
                // Handle network errors here
                Toast.makeText(getContext(), "Erreur lors du chargement des Pokémon", Toast.LENGTH_SHORT).show();
            }
        });
    }
}