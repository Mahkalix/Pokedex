package com.example.collectioncard.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonDetails {
    private Sprites sprites;
    private List<Type> types;
    private List<Ability> abilities;
    private List<Stat> stats;

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

    // Ajout de la méthode toString() pour afficher les détails dans les logs
    @Override
    public String toString() {
        StringBuilder details = new StringBuilder();
        details.append("Sprites: ").append(sprites != null ? sprites.getFrontDefault() : "No sprite available").append("\n");
        details.append("Types: ");
        if (types != null && !types.isEmpty()) {
            for (Type type : types) {
                details.append(type.getType().getName()).append(" ");
            }
        } else {
            details.append("No types available");
        }
        details.append("\nAbilities: ");
        if (abilities != null && !abilities.isEmpty()) {
            for (Ability ability : abilities) {
                details.append(ability.getAbility().getName()).append(" ");
            }
        } else {
            details.append("No abilities available");
        }
        details.append("\nStats: ");
        if (stats != null && !stats.isEmpty()) {
            for (Stat stat : stats) {
                details.append(stat.getStat().getName()).append(": ")
                        .append(stat.getBaseStat()).append(" (Effort: ")
                        .append(stat.getEffort()).append(") ");
            }
        } else {
            details.append("No stats available");
        }
        return details.toString();
    }

    private String getEffortStars(int effort) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < effort; i++) {
            stars.append("*");
        }
        return stars.toString();
    }

    // Sous-classe Sprites
    public static class Sprites {
        @SerializedName("front_default")
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
        @SerializedName("base_stat")
        private int baseStat;
        private int effort;

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

        public int getEffort() {
            return effort;
        }

        public void setEffort(int effort) {
            this.effort = effort;
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
