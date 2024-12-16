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
            // Récupérer l'email et le mot de passe
            String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (!databaseHelper.checkUser(email)) {  // Vérification uniquement par email
                User user = new User();
                user.setEmail(email);  // Utilisation de l'email
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
            // Récupérer l'email et le mot de passe
            String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (databaseHelper.checkUser(email, password)) {  // Connexion avec l'email
                // Sauvegarder la session avec une image par défaut
                saveUserSession(email, password, "default");  // Passer "default" pour l'image de profil

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Email or password is incorrect, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour sauvegarder la session de l'utilisateur
    private void saveUserSession(String email, String password, String profileImageUri) {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);  // État connecté
        editor.putString("email", email); // Sauvegarder l'email uniquement
        editor.putString("password", password); // Sauvegarder le mot de passe
        editor.putString("profileImageUri", profileImageUri); // Sauvegarder l'URI de l'image de profil
        editor.apply();
    }
}
