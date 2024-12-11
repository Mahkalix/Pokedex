package com.example.collectioncard.model;

import java.util.List;

public class Pokemon {
    private String name;
    private String url;
    private String imageUrl;
    private int number;


    // Constructeur
    public Pokemon(String name, String url) {
        this.name = name;
        this.url = url;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumber() {
        String[] urlPartes = url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length - 1]);
    }


}
