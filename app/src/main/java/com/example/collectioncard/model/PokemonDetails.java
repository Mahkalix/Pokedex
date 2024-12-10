package com.example.collectioncard.model;
import java.util.List;

public class PokemonDetails {
    private List<Type> types;
    private List<Ability> abilities;
    private List<Stat> stats;

    private Sprites sprites;
    private String name;

    public List<Type> getTypes() {
        return types;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public List<Stat> getStats() {
        return stats;
    }

    public static class Type {
        private TypeDetail type;

        public TypeDetail getType() {
            return type;
        }

        public static class TypeDetail {
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    public static class Ability {
        private AbilityDetail ability;

        public AbilityDetail getAbility() {
            return ability;
        }

        public static class AbilityDetail {
            private String name;

            public String getName() {
                return name;
            }
        }
    }

    public static class Stat {
        private int base_stat;
        private StatDetail stat;

        public int getBaseStat() {
            return base_stat;
        }

        public StatDetail getStat() {
            return stat;
        }

        public static class StatDetail {
            private String name;

            public String getName() {
                return name;
            }
        }
    }


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
