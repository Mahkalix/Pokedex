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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        // Set the root view to match the parent
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        root.setLayoutParams(params);

        final TextView textView = binding.TitleText;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        final TextView subtitleTextView = binding.SubtitleText;

        binding.mainImage.setImageResource(com.example.collectioncard.R.drawable.logo);

        final Button button = binding.button;
        Animation blinkAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.blink);
        button.startAnimation(blinkAnimation);

    button.setOnClickListener(v -> {

    NavHostFragment.findNavController(HomeFragment.this).navigate(R.id.navigation_profile);
});



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}