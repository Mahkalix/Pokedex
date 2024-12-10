package com.example.collectioncard.model;

public class PokemonDetails {
    private Sprites sprites;
    private String name;

    public String getName() {
        return name;
    }
    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public static class Sprites {
        private String front_default;

        public String getFrontDefault() {
            return front_default;
        }

        public void setFrontDefault(String front_default) {
            this.front_default = front_default;
        }
    }
}
