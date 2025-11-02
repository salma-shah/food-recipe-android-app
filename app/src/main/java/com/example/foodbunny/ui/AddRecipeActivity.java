package com.example.foodbunny.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodbunny.R;

public class AddRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        int recipeId = getIntent().getIntExtra("recipe_id", -1);

        // Create fragment and send arguments
        AddRecipeFragment addRecipeFragment = new AddRecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("recipe_id", recipeId);
        addRecipeFragment.setArguments(bundle);

        // Load fragment into container
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.add_recipe_container, addRecipeFragment)
                .commit();
    }
}