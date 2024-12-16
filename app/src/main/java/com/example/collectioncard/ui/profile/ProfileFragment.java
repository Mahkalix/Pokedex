package com.example.collectioncard.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.collectioncard.R;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1; // Code pour l'Intent de sélection d'image

    private TextView txtEmail, txtPassword;
    private ImageView profileImage;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Liaison avec la vue fragment_profile.xml
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialisation des TextViews et ImageView
        txtEmail = rootView.findViewById(R.id.txtEmail);
        txtPassword = rootView.findViewById(R.id.txtPassword); // Assurez-vous d'avoir ce TextView dans votre layout XML
        profileImage = rootView.findViewById(R.id.profileImage);
        Button btnChangeProfileImage = rootView.findViewById(R.id.btnChangeProfileImage);

        // Récupération des SharedPreferences
        preferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Charger les données utilisateur et l'image
        fetchUserData();

        // Listener pour changer l'image de profil
        btnChangeProfileImage.setOnClickListener(v -> openImageSelector());

        return rootView;
    }

    private void fetchUserData() {
        // Récupérer les valeurs depuis SharedPreferences
        String email = preferences.getString("email", "Non défini");  // Valeur par défaut si l'email n'est pas défini
        String password = preferences.getString("password", "Non défini");
        String profileImageUri = preferences.getString("profileImageUri", null);

        // Mettre à jour les champs texte
        txtEmail.setText(!email.equals("Non défini") ? email : "Aucune donnée enregistrée");
        txtPassword.setText(!password.equals("Non défini") ? "********" : "Aucun mot de passe");

        // Charger l'image depuis le chemin URI enregistré
        if (profileImageUri != null) {
            profileImage.setImageURI(Uri.parse(profileImageUri));
        } else {
            profileImage.setImageResource(R.drawable.default_profile_image); // Image par défaut
        }
    }

    private void openImageSelector() {
        // Intent pour ouvrir la galerie
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*"); // Permet uniquement de choisir des images
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                // Mettre à jour l'image affichée
                profileImage.setImageURI(selectedImageUri);

                // Sauvegarder l'URI de l'image sélectionnée dans SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("profileImageUri", selectedImageUri.toString());
                editor.apply();

                Toast.makeText(getContext(), "Image de profil mise à jour", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
