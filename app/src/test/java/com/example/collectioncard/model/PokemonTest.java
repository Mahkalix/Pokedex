package com.example.collectioncard.model;

import org.junit.Test;
import java.util.Locale;
import static org.junit.Assert.*;

public class PokemonTest {
    @Test public void extractsNumberWithOrWithoutTrailingSlash() {
        assertEquals(25, new Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/").getNumber());
        assertEquals(25, new Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25").getNumber());
        assertEquals(-1, new Pokemon("invalid", null).getNumber());
        assertEquals(-1, new Pokemon("invalid", "not-a-number").getNumber());
    }

    @Test public void searchesNamesAccentsAndPaddedNumbers() {
        Pokemon pokemon = new Pokemon("évoli", "https://pokeapi.co/api/v2/pokemon/133/");
        assertTrue(pokemon.matches("EVOLI"));
        assertTrue(pokemon.matches("#0133"));
        assertTrue(pokemon.matches(" 133 "));
        assertTrue(pokemon.matches(""));
        assertFalse(pokemon.matches("13"));
        assertFalse(pokemon.matches("999999999999999999999999999"));
    }

    @Test public void caseConversionDoesNotDependOnDeviceLocale() {
        Locale previous = Locale.getDefault();
        try {
            Locale.setDefault(new Locale("tr", "TR"));
            assertTrue(new Pokemon("pikachu", "https://pokeapi.co/api/v2/pokemon/25/").matches("PIKACHU"));
        } finally {
            Locale.setDefault(previous);
        }
    }
}
