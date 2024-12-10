package com.example.collectioncard.model;

import java.util.List;

public class Pokemon {
    private String name;
    private String url;
    private String imageUrl;
    private int number;
    private List<String> types;
    private List<String> abilities;
    private Stats stats;

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

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getNumber() {
        String[] urlPartes = url.split("/");
        return Integer.parseInt(urlPartes[urlPartes.length - 1]);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    // Classe interne pour représenter les statistiques
    public static class Stats {
        private int hp;
        private int attack;
        private int defense;
        private int specialAttack;
        private int specialDefense;
        private int speed;

        // Getters et setters
        public int getHp() {
            return hp;
        }

        public void setHp(int hp) {
            this.hp = hp;
        }

        public int getAttack() {
            return attack;
        }

        public void setAttack(int attack) {
            this.attack = attack;
        }

        public int getDefense() {
            return defense;
        }

        public void setDefense(int defense) {
            this.defense = defense;
        }

        public int getSpecialAttack() {
            return specialAttack;
        }

        public void setSpecialAttack(int specialAttack) {
            this.specialAttack = specialAttack;
        }

        public int getSpecialDefense() {
            return specialDefense;
        }

        public void setSpecialDefense(int specialDefense) {
            this.specialDefense = specialDefense;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }
    }
}
