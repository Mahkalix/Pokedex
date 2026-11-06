package com.example.collectioncard.authentification;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordHasherTest {
    @Test public void hashUsesUniqueSaltAndPreservesPasswordWhitespace() {
        String first = PasswordHasher.hash(" secret-test ");
        String second = PasswordHasher.hash(" secret-test ");
        assertNotEquals(first, second);
        assertTrue(PasswordHasher.verify(" secret-test ", first));
        assertFalse(PasswordHasher.verify("secret-test", first));
        assertFalse(first.contains("secret-test"));
    }

    @Test public void invalidHashesAndPasswordsAreRejected() {
        assertFalse(PasswordHasher.verify("secret", "secret"));
        assertFalse(PasswordHasher.verify("secret", "pbkdf2-sha1$1300000$invalid$invalid"));
        assertFalse(PasswordHasher.verify("secret", "pbkdf2-sha1$999999999$00000000000000000000000000000000$" + "0".repeat(64)));
        assertFalse(PasswordHasher.verify(null, null));
    }
}
