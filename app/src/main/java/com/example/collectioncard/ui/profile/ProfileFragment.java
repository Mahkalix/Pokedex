package com.example.collectioncard.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.collectioncard.R;

public class ProfileFragment extends Fragment {

    private TextView txtEmail, txtPassword;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Liaison avec la vue fragment_profile.xml
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialisation des TextViews
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPassword = rootView.findViewById(R.id.txtPassword); // Assurez-vous d'avoir ce TextView dans votre layout XML

        // Récupérer et afficher les données de l'utilisateur
        fetchUserData();

        return rootView;
    }

    private void fetchUserData() {
        SharedPreferences preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Récupérer les valeurs depuis SharedPreferences
        String email = preferences.getString("email", "Non défini");  // Valeur par défaut si l'email n'est pas défini
        String password = preferences.getString("password", "Non défini");

        // Vérifier si les données sont valides
        if (!email.equals("Non défini") && !password.equals("Non défini")) {
            // Afficher les données dans les TextViews si elles sont correctement récupérées
            txtEmail.setText(email);
            txtPassword.setText(password);
        } else {
            // Afficher un message d'erreur ou un message par défaut si les données sont manquantes
            txtEmail.setText("Aucune donnée enregistrée");
            txtPassword.setText("Aucun mot de passe");
        }
    }
}
