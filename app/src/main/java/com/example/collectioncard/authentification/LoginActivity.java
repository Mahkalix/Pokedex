package com.example.collectioncard.authentification;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.collectioncard.MainActivity;
import com.example.collectioncard.R;
import com.example.collectioncard.model.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private DatabaseHelper database;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = new DatabaseHelper(this);
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().remove("password").apply();
        findViewById(R.id.registerButton).setOnClickListener(v -> authenticate(true));
        findViewById(R.id.loginButton).setOnClickListener(v -> authenticate(false));
    }

    private void authenticate(boolean register) {
        EditText emailField = findViewById(R.id.emailEditText);
        EditText passwordField = findViewById(R.id.passwordEditText);
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Saisissez une adresse e-mail valide");
            return;
        }
        if (password.isEmpty() || (register && password.length() < 8)) {
            passwordField.setError(register ? "Utilisez au moins 8 caractères" : "Saisissez votre mot de passe");
            return;
        }
        setBusy(true);
        worker.execute(() -> {
            try {
                boolean success;
                if (register) {
                    User user = new User();
                    user.setEmail(email);
                    user.setPassword(password);
                    success = database.addUser(user);
                } else {
                    success = database.checkUser(email, password);
                }
                runOnUiThread(() -> {
                    if (isFinishing() || isDestroyed()) return;
                    setBusy(false);
                    passwordField.setText("");
                    if (register) {
                        toast(success ? "Compte créé. Vous pouvez vous connecter." : "Cette adresse possède déjà un compte.");
                    } else if (success) {
                        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit()
                                .putBoolean("isLoggedIn", true).putString("email", email).remove("password").apply();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("navigateToDashboard", true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        toast("Adresse e-mail ou mot de passe incorrect.");
                    }
                });
            } catch (RuntimeException e) {
                runOnUiThread(() -> {
                    if (!isFinishing() && !isDestroyed()) {
                        setBusy(false);
                        toast("Impossible d’accéder au compte. Réessayez.");
                    }
                });
            }
        });
    }

    private void setBusy(boolean busy) {
        findViewById(R.id.registerButton).setEnabled(!busy);
        findViewById(R.id.loginButton).setEnabled(!busy);
    }

    private void toast(String message) { Toast.makeText(this, message, Toast.LENGTH_SHORT).show(); }

    @Override protected void onDestroy() {
        super.onDestroy();
        worker.execute(() -> database.close());
        worker.shutdown();
    }
}
