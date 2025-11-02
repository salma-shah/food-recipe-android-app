package com.example.foodbunny.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.adapters.EditRecipesAdapter;
import com.example.foodbunny.objects.Recipe;

import java.util.List;

public class EditYourRecipesActivity extends AppCompatActivity {
    private ImageButton btnGoBackToProfile;
    private RecyclerView rvMyRecipes;
    private View viewEmpty;
    private DBHelper dbHelper;
    private Context context;
    private EditRecipesAdapter editRecipesAdapter;
    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_your_recipes);

        context = this;

        // initializing
        dbHelper = new DBHelper(this);
        btnGoBackToProfile = findViewById(R.id.btnGoBackToProfile);
        rvMyRecipes = findViewById(R.id.rvMyRecipes);
        viewEmpty = findViewById(R.id.viewEmpty);

        SharedPreferences sharedPreferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        int userId = dbHelper.getUserId(userEmail);
        Log.d("EditYourRecipesActivity", "The user Id is: " +userId);

        // setting up the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvMyRecipes.setLayoutManager(linearLayoutManager);

        recipeList = dbHelper.getAllMyRecipes(userId);
        editRecipesAdapter = new EditRecipesAdapter(context, recipeList);
        rvMyRecipes.setLayoutManager(new GridLayoutManager(context, 1));
        rvMyRecipes.setAdapter(editRecipesAdapter);
        checkEmptyState();

        // if no recipes yet
        editRecipesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmptyState();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmptyState();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmptyState();
            }
            
        });

        // user returns to their profile page
        btnGoBackToProfile.setOnClickListener(v->
        {
            Intent intent = new Intent(EditYourRecipesActivity.this, NavigationBar.class);
            startActivity(intent);
        });
    }

    private void checkEmptyState() {
        if (editRecipesAdapter.getItemCount() == 0) {
            rvMyRecipes.setVisibility(View.GONE);
            viewEmpty.setVisibility(View.VISIBLE);
        } else {
            rvMyRecipes.setVisibility(View.VISIBLE);
            viewEmpty.setVisibility(View.GONE);
        }
    }
}