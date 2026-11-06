package com.example.collectioncard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.collectioncard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().remove("password").apply();
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        binding.navView.setItemIconTintList(null);
        NavigationUI.setupWithNavController(binding.navView, controller);
        controller.addOnDestinationChangedListener((nav, destination, arguments) ->
                binding.navView.setVisibility(destination.getId() == R.id.navigation_home ? View.GONE : View.VISIBLE));
        boolean loggedIn = getSharedPreferences("UserPrefs", MODE_PRIVATE).getBoolean("isLoggedIn", false);
        if (savedInstanceState == null && (loggedIn || getIntent().getBooleanExtra("navigateToDashboard", false))) {
            controller.navigate(R.id.navigation_dashboard);
        }
    }
}
