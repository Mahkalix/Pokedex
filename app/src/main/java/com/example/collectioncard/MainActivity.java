package com.example.collectioncard;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.collectioncard.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // Utilisation de View Binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurer les paramètres de mise en page pour correspondre au parent
        ViewGroup.LayoutParams params = binding.getRoot().getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        binding.getRoot().setLayoutParams(params);

        // Configurer le BottomNavigationView
        BottomNavigationView navView = binding.navView;

        // Pour afficher les icônes en couleur
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setItemIconTintList(null);

        // Définir les destinations de niveau supérieur (si nécessaire pour AppBarConfiguration)
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile)
                .build();

        // Récupérer le NavController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

        // Lier le BottomNavigationView au NavController
        NavigationUI.setupWithNavController(navView, navController);

        // Cacher ou afficher la barre de navigation pour certaines destinations
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_home) {
                navView.setVisibility(View.GONE); // Cacher le BottomNavigationView pour HomeFragment
            } else {
                navView.setVisibility(View.VISIBLE); // Montrer pour les autres fragments
            }
        });
    }
}
