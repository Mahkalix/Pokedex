package com.example.collectioncard.authentification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.collectioncard.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), "UserManager.db", null, 2);
    }

    @Override public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA secure_delete=ON");
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(user_name TEXT PRIMARY KEY, user_password TEXT NOT NULL)");
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // SQLiteOpenHelper already wraps the migration in a transaction.
            try (Cursor cursor = db.query("user", new String[]{"user_name", "user_password"}, null, null, null, null, null)) {
                while (cursor.moveToNext()) {
                    ContentValues values = new ContentValues();
                    values.put("user_password", PasswordHasher.hash(cursor.isNull(1) ? "" : cursor.getString(1)));
                    db.update("user", values, "user_name = ?", new String[]{cursor.getString(0)});
                }
            }
        }
    }

    public boolean addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("user_name", user.getEmail());
        values.put("user_password", PasswordHasher.hash(user.getPassword()));
        return getWritableDatabase().insertWithOnConflict("user", null, values, SQLiteDatabase.CONFLICT_IGNORE) != -1;
    }

    public boolean checkUser(String email) {
        try (Cursor cursor = getReadableDatabase().query("user", new String[]{"user_name"}, "user_name = ?", new String[]{email}, null, null, null)) {
            return cursor.moveToFirst();
        }
    }

    public boolean checkUser(String email, String password) {
        try (Cursor cursor = getReadableDatabase().query("user", new String[]{"user_password"}, "user_name = ?", new String[]{email}, null, null, null)) {
            return cursor.moveToFirst() && PasswordHasher.verify(password, cursor.getString(0));
        }
    }
}
