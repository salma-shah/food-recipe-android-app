package com.example.foodbunny.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.adapters.RecipeAdapter;
import com.example.foodbunny.objects.Recipe;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecipeAdapter recipeAdapter;
    private DBHelper dbHelper;
    private List<Recipe> recipeList;
    private TextView tvUsername;
    private SearchView svSearchBar;
    private RecyclerView rvRecipes;
    private ImageButton btnCatBeverage, btnCatDinner, btnCatBreakfast, btnCatDessert, btnCatSnack, btnFilter;
    private String selectedCategory = "";
    private String selectedType = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DBHelper(requireActivity());
        tvUsername = view.findViewById(R.id.tvUsername);
        svSearchBar = view.findViewById(R.id.svSearchBar);
        rvRecipes = view.findViewById(R.id.rvRecipes);
        btnCatBreakfast = view.findViewById(R.id.btnCatBreakfast);
        btnCatDessert = view.findViewById(R.id.btnCatDessert);
        btnCatSnack = view.findViewById(R.id.btnCatSnack);
        btnCatBeverage = view.findViewById(R.id.btnCatBeverage);
        btnCatDinner = view.findViewById(R.id.btnCatDinner);
        btnFilter = view.findViewById(R.id.btnFilter);

        // getting user name
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        if (userEmail != null) {
            String fullName = dbHelper.getUserName(userEmail);
            tvUsername.setText("Hi, " + fullName);
        } else {
            tvUsername.setText("Hi, User!");
        }

        // filling recycle view
        if (recipeList == null) {
            // array list
            recipeList = new ArrayList<>();
        }
        recipeAdapter = new RecipeAdapter(requireContext(), recipeList);
        rvRecipes.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        rvRecipes.setAdapter(recipeAdapter);

        // search and filter category and type function
        setUpSearch();
        setUpCategoryBtns(view);
        setUpFilterBtn(view);
        loadRecipes("", "", "");

        return view;
    }
    private void loadRecipes(String category, String type, String searchQuery) {
        List<Recipe> filtered = dbHelper.getFilteredRecipes(category,type, searchQuery);
        recipeAdapter.updateData(filtered);
    }

    private void setUpSearch() {
        svSearchBar.setQueryHint("Search...");
        svSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadRecipes(selectedCategory, selectedType, query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                loadRecipes(selectedCategory, selectedType, newText);
                return true;
            }
        });
    }

    // category
    private void setUpCategoryBtns(View view) {
        setCategoryListener(btnCatDinner, "Dinner");
        setCategoryListener(btnCatBreakfast, "Breakfast");
        setCategoryListener(btnCatSnack, "Snack");
        setCategoryListener(btnCatDessert, "Dessert");
        setCategoryListener(btnCatBeverage, "Beverage");
    }

    private void setCategoryListener(ImageButton button, String category) {
        button.setOnClickListener(v-> {
            if (selectedCategory.equals(category))
            {
                selectedCategory = "";
            }
            else {  selectedCategory = category;  }

            resetAllButtonHighlights();
            if (!selectedCategory.isEmpty()) {
                tintButton(button, R.color.secondary_colour);
            }

            String searchText = svSearchBar.getQuery().toString();
            loadRecipes(selectedCategory, selectedType, searchText);
        });
    }

    // filter
    private void setUpFilterBtn(View view)
    {
        btnFilter.setOnClickListener(v -> {

            // filling the pop up menu with the options
            PopupMenu popup = new PopupMenu(requireContext(), v);
            popup.getMenu().add("Dairy");
            popup.getMenu().add("Healthy");
            popup.getMenu().add("Seafood");
            popup.getMenu().add("Easy to Make");
            popup.getMenu().add("Overnight");
            popup.getMenu().add("Vegan");

            popup.setOnMenuItemClickListener(item -> {
                String type = item.getTitle().toString();

                // making sure selected type is selected
                if (selectedType.equals(type))
                {
                    selectedType = "";
                }
                else
                {
                    selectedType = type;
                }

                // if a type is selected, the filter btn will be coloured.
                // otherwise, it will go back to default colour
                if (selectedType.isEmpty())
                {
                    btnFilter.setColorFilter(ContextCompat.getColor(requireContext(), R.color.black));
                }
                else
                {
                    btnFilter.setColorFilter(ContextCompat.getColor(requireContext(), R.color.primary_colour));
                }

                // connencting with the search query
                String searchText = svSearchBar.getQuery().toString();
                loadRecipes(selectedCategory, selectedType, searchText);
                return true;
            });

            popup.show();
        });
    }

    // category button colours; resetting back to default colour
    private void resetAllButtonHighlights() {
        resetButtonTint(btnCatBreakfast);
        resetButtonTint(btnCatDessert);
        resetButtonTint(btnCatSnack);
        resetButtonTint(btnCatBeverage);
        resetButtonTint(btnCatDinner);
    }

    private void resetButtonTint(ImageButton button) {
        Drawable background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle);
        if (background != null) {
            background = background.mutate();
            background.setTint(ContextCompat.getColor(requireContext(), R.color.cat_colour));
            button.setBackground(background);
        }
    }

    // ; highlighting the selected cat btn colour
    private void tintButton(ImageButton button, int colorRes) {
        Drawable background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_circle);
        if (background != null) {
            background = background.mutate();
            background.setTint(ContextCompat.getColor(requireContext(), colorRes));
            button.setBackground(background);
        }
    }

}