package com.example.foodbunny.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.R;
import com.example.foodbunny.ui.RecipeDetailsActivity;
import com.example.foodbunny.objects.Favourite;
import com.example.foodbunny.objects.Recipe;

import java.io.File;
import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<Favourite> favouriteList;
    private Context context;

    public FavouriteAdapter(Context context, List<Favourite> favouriteList)
    {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new FavouriteAdapter.FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.FavouriteViewHolder holder, int position) {
        Favourite fav = favouriteList.get(position);
        Recipe recipe = fav.getRecipe();

        // getting details of the recipe
        holder.tvRecipeName.setText(recipe.getName());

        // getting the drawable image's name
        // or the image's path
        // it depends on how it is stored in the database

        String imagePath = recipe.getRecipeImage();
        if (imagePath != null)
        {
            if (imagePath.startsWith("images"))
            {
                File file = new File(context.getFilesDir(), imagePath);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    holder.imgRecipe.setImageBitmap(bitmap);
                }
            }
            else
            {
                int imageResId = context.getResources().getIdentifier(recipe.getRecipeImage(), "drawable", context.getPackageName());
                if (imageResId != 0)
                {
                    holder.imgRecipe.setImageResource(imageResId);
                }
            }
        }
        else
        {
            holder.imgRecipe.setImageResource(R.drawable.ic_confused_bunny);
        }

        holder.imgRecipe.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
            // passing the recipe id
            intent.putExtra("recipe_id", recipe.getId());
            v.getContext().startActivity(intent);
        });

        holder.tvRecipeName.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), RecipeDetailsActivity.class);
            // passing the recipe id
            intent.putExtra("recipe_id", recipe.getId());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public void updateData(List<Favourite> newList) {
        Log.d("FavouriteAdapter", "Updating list, new size: " + newList.size());
        favouriteList = newList;
        notifyDataSetChanged();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;
        ImageView imgRecipe;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
        }
    }
}
