package com.example.collectioncard.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.collectioncard.PokemonAdapter;
import com.example.collectioncard.R;
import com.example.collectioncard.model.PokemonResponse;
import com.example.collectioncard.network.ApiClient;
import com.example.collectioncard.network.PokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private Call<PokemonResponse> request;
    private PokemonAdapter adapter;
    private View root;
    private String query = "";

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle state) {
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        if (state != null) query = state.getString("query", "");
        RecyclerView grid = root.findViewById(R.id.recyclerViewPokemonsDashboard);
        grid.setLayoutManager(new GridLayoutManager(requireContext(), getResources().getConfiguration().screenWidthDp >= 600 ? 3 : 2));
        SearchView search = root.findViewById(R.id.searchViewPokemon);
        search.setQuery(query, false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String text) { search.clearFocus(); return true; }
            @Override public boolean onQueryTextChange(String text) {
                query = text;
                if (adapter != null) adapter.getFilter().filter(text);
                return true;
            }
        });
        root.findViewById(R.id.retryButton).setOnClickListener(v -> load());
        load();
        return root;
    }

    private void load() {
        if (request != null) request.cancel();
        root.findViewById(R.id.loadingImage).setVisibility(View.VISIBLE);
        root.findViewById(R.id.statusText).setVisibility(View.GONE);
        root.findViewById(R.id.retryButton).setVisibility(View.GONE);
        request = ApiClient.getRetrofitInstance().create(PokeApiService.class).getPokemons();
        request.enqueue(new Callback<PokemonResponse>() {
            @Override public void onResponse(@NonNull Call<PokemonResponse> call, @NonNull Response<PokemonResponse> response) {
                if (root == null || call != request) return;
                root.findViewById(R.id.loadingImage).setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body() == null || response.body().getResults() == null) {
                    showError();
                    return;
                }
                adapter = new PokemonAdapter(response.body().getResults(), () -> {
                    if (root == null) return;
                    TextView status = root.findViewById(R.id.statusText);
                    status.setText("Aucun Pokémon trouvé. Essayez un autre nom ou numéro.");
                    status.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                });
                ((RecyclerView) root.findViewById(R.id.recyclerViewPokemonsDashboard)).setAdapter(adapter);
                adapter.getFilter().filter(query);
            }
            @Override public void onFailure(@NonNull Call<PokemonResponse> call, @NonNull Throwable error) {
                if (root != null && call == request && !call.isCanceled()) showError();
            }
        });
    }

    private void showError() {
        root.findViewById(R.id.loadingImage).setVisibility(View.GONE);
        TextView status = root.findViewById(R.id.statusText);
        status.setText("Impossible de charger le Pokédex. Vérifiez votre connexion.");
        status.setVisibility(View.VISIBLE);
        root.findViewById(R.id.retryButton).setVisibility(View.VISIBLE);
    }

    @Override public void onSaveInstanceState(@NonNull Bundle state) {
        super.onSaveInstanceState(state);
        state.putString("query", query);
    }

    @Override public void onDestroyView() {
        if (request != null) request.cancel();
        request = null;
        if (root != null) ((RecyclerView) root.findViewById(R.id.recyclerViewPokemonsDashboard)).setAdapter(null);
        adapter = null;
        root = null;
        super.onDestroyView();
    }
}
