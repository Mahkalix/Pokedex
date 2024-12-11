package com.example.collectioncard.model;

import java.util.List;

public class PokemonDetails {

    private String name;
    private Sprites sprites;
    private List<Type> types;
    private List<Ability> abilities;
    private List<Stat> stats;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public void setSprites(Sprites sprites) {
        this.sprites = sprites;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public void setStats(List<Stat> stats) {
        this.stats = stats;
    }

    // Sous-classe Sprites
    public static class Sprites {
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }

    // Sous-classe Type
    public static class Type {
        private TypeInfo type;

        public TypeInfo getType() {
            return type;
        }

        public void setType(TypeInfo type) {
            this.type = type;
        }

        public static class TypeInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    // Sous-classe Ability
    public static class Ability {
        private AbilityInfo ability;

        public AbilityInfo getAbility() {
            return ability;
        }

        public void setAbility(AbilityInfo ability) {
            this.ability = ability;
        }

        public static class AbilityInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    // Sous-classe Stat
    public static class Stat {
        private StatInfo stat;
        private int baseStat;

        public StatInfo getStat() {
            return stat;
        }

        public void setStat(StatInfo stat) {
            this.stat = stat;
        }

        public int getBaseStat() {
            return baseStat;
        }

        public void setBaseStat(int baseStat) {
            this.baseStat = baseStat;
        }

        public static class StatInfo {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
