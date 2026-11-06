package com.example.collectioncard.authentification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.collectioncard.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AccountMigrationTest {
    @Test public void upgradesLegacyAccountWithoutLosingItAndRejectsDuplicates() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String name = "migration-test-" + System.nanoTime() + ".db";
        try {
            try (SQLiteDatabase legacy = context.openOrCreateDatabase(name, Context.MODE_PRIVATE, null)) {
                legacy.execSQL("CREATE TABLE user(user_name TEXT PRIMARY KEY, user_password TEXT)");
                legacy.execSQL("INSERT INTO user VALUES (?, ?)", new Object[]{"test@example.org", " old-password "});
                legacy.setVersion(1);
            }
            try (DatabaseHelper helper = new DatabaseHelper(context, name)) {
                assertTrue(helper.checkUser("test@example.org", " old-password "));
                assertFalse(helper.checkUser("test@example.org", "old-password"));
                assertFalse(helper.checkUser("missing@example.org", " old-password "));
                try (Cursor cursor = helper.getReadableDatabase().rawQuery("SELECT user_password FROM user", null)) {
                    assertTrue(cursor.moveToFirst());
                    assertTrue(cursor.getString(0).startsWith("pbkdf2-sha1$"));
                    assertNotEquals(" old-password ", cursor.getString(0));
                }
                User duplicate = new User();
                duplicate.setEmail("test@example.org");
                duplicate.setPassword("replacement");
                assertFalse(helper.addUser(duplicate));
            }
            try (DatabaseHelper reopened = new DatabaseHelper(context, name)) {
                assertTrue(reopened.checkUser("test@example.org", " old-password "));
            }
        } finally {
            context.deleteDatabase(name);
        }
    }
}
