package com.example.collectioncard.model;

import java.util.List;

public class PokemonResponse {
    private List<Pokemon> results;

    // Getter et Setter
    public List<Pokemon> getResults() {
        return results;
    }

    public void setResults(List<Pokemon> results) {
        this.results = results;
    }
}
