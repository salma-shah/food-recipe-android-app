package com.example.foodbunny.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodbunny.databinding.ActivityNavigationBarBinding;

import com.example.foodbunny.R;

public class NavigationBar extends AppCompatActivity {

    ActivityNavigationBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNavigationBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.frameLayout, new HomeFragment())
                    .commit();

            binding.bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new HomeFragment())
                            .commit();
                } else if (itemId == R.id.chatbot) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new ChatBotFragment())
                            .commit();
                } else if (itemId == R.id.profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new ProfileFragment())
                            .commit();
                }
                else if (itemId == R.id.recipeAdd) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new AddRecipeFragment())
                            .commit();
                }
                else if (itemId == R.id.favourites) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new FavouritesFragment())
                            .commit();
                }
                return true;
            });
        }
    }
}