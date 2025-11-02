package com.example.foodbunny.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.adapters.FavouriteAdapter;
import com.example.foodbunny.objects.Favourite;

import java.util.List;

public class FavouritesFragment extends Fragment {
    private RecyclerView rvFavRecipes;
    private DBHelper dbHelper;
    private List<Favourite> favouriteList;
    private FavouriteAdapter favouriteAdapter;
    private View viewFavEmpty;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        dbHelper = new DBHelper(requireContext());

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        int userId = dbHelper.getUserId(userEmail);
        Log.d("FavouritesFragment", "The user Id is: " +userId);

        rvFavRecipes = view.findViewById(R.id.rvFavRecipes);
        viewFavEmpty = view.findViewById(R.id.viewFavEmpty);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvFavRecipes.setLayoutManager(linearLayoutManager);

        favouriteList = dbHelper.getAllMyFavourites(userId);
        favouriteAdapter = new FavouriteAdapter(getContext(), favouriteList);
        rvFavRecipes.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
        rvFavRecipes.setAdapter(favouriteAdapter);
        checkEmptyState();

        // if no recipes yet
        favouriteAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
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

        return view;
    }

    private void checkEmptyState() {
        if (favouriteAdapter.getItemCount() == 0) {
            rvFavRecipes.setVisibility(View.GONE);
            viewFavEmpty.setVisibility(View.VISIBLE);
        } else {
            rvFavRecipes.setVisibility(View.VISIBLE);
            viewFavEmpty.setVisibility(View.GONE);
        }
    }
}