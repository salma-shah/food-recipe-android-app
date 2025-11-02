package com.example.foodbunny.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.R;
import com.example.foodbunny.ui.RecipeDetailsActivity;
import com.example.foodbunny.objects.Recipe;

import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
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
        return recipeList.size();
    }

    public void updateData(List<Recipe> newList) {
        Log.d("RecipeAdapter", "Updating list, new size: " + newList.size());
        recipeList = newList;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecipeName;
        ImageView imgRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
        }
    }
}
