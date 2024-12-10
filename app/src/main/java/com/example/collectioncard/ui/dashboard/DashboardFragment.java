package com.example.collectioncard.ui.dashboard;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Configurer la RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerViewPokemonsDashboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Appel à l'API pour récupérer les Pokémon
        fetchPokemonData();

        return rootView;
    }

    private void fetchPokemonData() {
        PokeApiService service = ApiClient.getRetrofitInstance().create(PokeApiService.class);
        Call<PokemonResponse> call = service.getPokemons();

        call.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pokemonList = response.body().getResults();

                    // Créer un adapter pour afficher les données dans la RecyclerView
                    pokemonAdapter = new PokemonAdapter(pokemonList);
                    recyclerView.setAdapter(pokemonAdapter);
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                // Gérer les erreurs réseau ici
            }
        });
    }
}
