package com.example.collectioncard.authentification;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collectioncard.MainActivity;
import com.example.collectioncard.R;
import com.example.collectioncard.model.User;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);

        btnRegisterListener();
        btnLoginListener();
    }

    private void btnRegisterListener() {
        findViewById(R.id.registerButton).setOnClickListener(view -> {
            // Utilisation de getText().toString() pour récupérer le texte de l'EditText
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (!databaseHelper.checkUser(username)) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);

                databaseHelper.addUser(user);
                Toast.makeText(LoginActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void btnLoginListener() {
        findViewById(R.id.loginButton).setOnClickListener(view -> {
            String username = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (databaseHelper.checkUser(username, password)) {
                // Sauvegarder la session avec une image par défaut
                saveUserSession(username, password, "default");  // Passer "default" pour l'image de profil

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Username or password is incorrect, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Nouvelle méthode pour sauvegarder la session de l'utilisateur
    private void saveUserSession(String username, String password, String profileImageUri) {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);  // État connecté
        editor.putString("username", username); // Enregistrer l'username
        editor.putString("email", username + "@example.com"); // Enregistrer l'email
        editor.putString("password", password); // Enregistrer le mot de passe
        editor.putString("profileImageUri", profileImageUri); // Sauvegarder l'URI de l'image du profil
        editor.apply();
    }
}
