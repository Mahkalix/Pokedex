package com.example.collectioncard.ui.home;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.collectioncard.R;
import com.example.collectioncard.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Rendre toute la vue cliquable
        View rootLayout = binding.rootLayout;;
        rootLayout.setOnClickListener(v -> {
            // Action à effectuer lors du clic
            Log.d(TAG, "La vue entière a été cliquée !");
            NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.navigation_dashboard);
        });

        // Code existant
        final TextView textView = binding.TitleText;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final TextView subtitleTextView = binding.SubtitleText;
        binding.mainImage.setImageResource(com.example.collectioncard.R.drawable.logo);

        final Button button = binding.button;
        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        button.startAnimation(blinkAnimation);

        // Supprime le setOnClickListener du bouton si tout est cliquable
        button.setOnClickListener(null);

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}