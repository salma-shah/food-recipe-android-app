package com.example.foodbunny.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.ui.AddRecipeActivity;
import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.objects.Recipe;

import java.io.File;
import java.util.List;

public class EditRecipesAdapter extends RecyclerView.Adapter<EditRecipesAdapter.EditRecipesViewHolder> {
    private DBHelper dbHelper;
    private Context context;
    private List<Recipe> recipeList;

    public EditRecipesAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public EditRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_edit_recipe_, parent, false);
        return new EditRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EditRecipesAdapter.EditRecipesViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        dbHelper = new DBHelper(context);

        holder.tvRecipeName.setText(recipe.getName());
        holder.tvPostedDate.setText("Posted on " + recipe.getDatePosted());

        int commentCount = dbHelper.getCommentsCountForRecipe(recipe.getId());
        holder.tvComments.setText("Comments (" + commentCount + ")");

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

        holder.btnDelete.setOnClickListener(v-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("DeleteRecipe").
                    setMessage("Are you sure that you want to delete this recipe?");
            builder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int recipeId = recipe.getId();
                            long result = dbHelper.deleteRecipe(recipeId);

                            if (result != -1)
                            {
                                Toast.makeText(context, "The recipe was deleted!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "There was an error with deleting the recipe.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            builder.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder.create();
            alert11.show();
        });

        holder.btnEdit.setOnClickListener(v-> {
            Intent intent = new Intent(v.getContext(), AddRecipeActivity.class);
            intent.putExtra("recipe_id", recipe.getId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class EditRecipesViewHolder extends RecyclerView.ViewHolder {

        TextView tvRecipeName, tvPostedDate, tvComments;
        ImageView imgRecipe;
        ImageButton btnEdit, btnDelete;

        public EditRecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRecipe = itemView.findViewById(R.id.imgRecipe);
            tvRecipeName = itemView.findViewById(R.id.tvRecipeName);
            tvPostedDate = itemView.findViewById(R.id.tvPostedDate);
            tvComments = itemView.findViewById(R.id.tvComments);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
