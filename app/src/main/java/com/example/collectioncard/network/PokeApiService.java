package com.example.collectioncard.network;

import com.example.collectioncard.model.PokemonDetails;
import com.example.collectioncard.model.PokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface PokeApiService {

    // Récupérer la liste des Pokémon
    @GET("pokemon?limit=100")
    Call<PokemonResponse> getPokemons();

    // Récupérer les détails d'un Pokémon
    @GET
    Call<PokemonDetails> getPokemonDetails(@Url String url);
}
