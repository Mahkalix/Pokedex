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
            String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (!databaseHelper.checkUser(email)) {
                User user = new User();
                user.setEmail(email);
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
            String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
            String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString().trim();

            if (databaseHelper.checkUser(email, password)) {
                saveUserSession(email, password, "default");

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("navigateToDashboard", true);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "Email or password is incorrect, please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserSession(String email, String password, String profileImageUri) {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("profileImageUri", profileImageUri);
        editor.apply();
    }
}