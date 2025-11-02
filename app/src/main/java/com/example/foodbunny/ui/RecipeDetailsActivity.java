package com.example.foodbunny.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.adapters.CommentAdapter;
import com.example.foodbunny.objects.Comment;
import com.example.foodbunny.objects.Ingredient;
import com.example.foodbunny.objects.Recipe;

import java.io.File;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private Context context;
    private Recipe recipe;
    private int recipeId, userId;
    boolean isFavourite;
    private TextView tvDirections, tvIngredients, tvPrepTime, tvServings, tvCaption, tvRecipeName, tvComments;
    private ImageView imgRecipe;
    private EditText etAddComment;
    private ImageButton btnPostComment;
    private List<Comment> commentList;
    private CommentAdapter commentAdapter;
    private RecyclerView rvComments;
    private AppCompatButton btnFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);

        context = this;
        dbHelper = new DBHelper(this);
        tvRecipeName = findViewById(R.id.tvRecipeName);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvPrepTime = findViewById(R.id.tvPrepTime);
        tvServings = findViewById(R.id.tvServings);
        tvCaption = findViewById(R.id.tvCaption);
        tvDirections = findViewById(R.id.tvDirections);
        btnFavourite = findViewById(R.id.btnFavourite);
        imgRecipe = findViewById(R.id.imgRecipe);
        tvComments = findViewById(R.id.tvComments);
        etAddComment = findViewById(R.id.etAddComment);
        btnPostComment = findViewById(R.id.btnPostComment);

        SharedPreferences sharedPreferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        int userId = dbHelper.getUserId(userEmail);
        Log.d("RecipeDetailsActivity", "The user Id is: " +userId);

        recipeId = getIntent().getIntExtra("recipe_id", -1);
        Log.d("RecipeDetailsActivity", "The Id of the recipe is: " + recipeId);

        // as long as recipe id is valid, the information will fill the correct fields
        if (recipeId != -1)
        {
            recipe = dbHelper.getRecipeDetails(recipeId);
            Log.d("RecipeDetailsActivity", "The details of the recipe are: " + recipe);

            if (recipe != null)
            {

                // getting the drawable image's name
                // or the image's path
                // it depends on how it is stored in the database

                String imagePath = recipe.getRecipeImage();
                if (imagePath != null)
                {
                    if (imagePath.startsWith("images"))
                    {
                        File file = new File(getFilesDir(), imagePath);
                        if (file.exists()) {
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            imgRecipe.setImageBitmap(bitmap);
                        }
                    }
                    else
                    {
                        int imageResId = getResources().getIdentifier(recipe.getRecipeImage(), "drawable", getPackageName());
                        if (imageResId != 0)
                        {
                            imgRecipe.setImageResource(imageResId);
                        }
                    }
                }
                else
                {
                    imgRecipe.setImageResource(R.drawable.ic_confused_bunny);
                }

                tvRecipeName.setText(recipe.getName());
                tvCaption.setText(recipe.getCaption());
                tvPrepTime.setText(recipe.getDuration());

                if (recipe.getServingSize() == 1)
                {
                    tvServings.setText(recipe.getServingSize() + " PERSON ");
                }
                else
                {
                    tvServings.setText(recipe.getServingSize() + " PEOPLE ");
                }

                // now getting ingredients
                StringBuilder ingText = new StringBuilder();
                for (Ingredient ing : recipe.getIngredients()) {

                    String qtyFormatted;

                    double qty = ing.getQty();
                    if (Math.abs(qty - Math.round(qty)) < 0.000001)
                    {
                        qtyFormatted = String.valueOf((int) Math.round(qty));
                    }
                    else
                    {
                        qtyFormatted = String.valueOf(qty);
                    }

                    if (ing.getUnit() != null && !ing.getUnit().isEmpty()) {
                        ingText.append(" • ").append(qtyFormatted).append(" ")
                                .append(ing.getUnit()).append(" ")
                                .append(ing.getName()).append("\n");
                    }
                    else
                    {
                        ingText.append(" • ").append(qtyFormatted).append(" ")
                                .append(ing.getName()).append("\n");
                    }
                }
                tvIngredients.setText(ingText.toString());

                // now directions
                if (recipe.getDirections() != null && !recipe.getDirections().isEmpty())
                {
                    List<String> directions = recipe.getDirections();
                    StringBuilder dirFormatted = new StringBuilder();

                    for (int i = 0; i< directions.size() ; i++)
                    {
                        dirFormatted.append(i + 1)
                                .append(".")
                                .append(directions.get(i).trim())
                                .append("\n");
                    }
                    tvDirections.setText(dirFormatted.toString().trim());
                }
            }
            else
            {
                tvRecipeName.setText("No Recipe Name");
                tvCaption.setText("No Recipe Caption");
                tvPrepTime.setText("No Prep Time");
                tvServings.setText("No Serving Size");
                tvIngredients.setText("No Ingredients");
                tvDirections.setText("No Directions available");
            }
        }
        else
        {
            Toast.makeText(this, "Invalid Recipe Id", Toast.LENGTH_SHORT).show();
        }

        boolean isFavourite = dbHelper.checkIfFav(userId, recipeId);
        if (isFavourite)
        {
            btnFavourite.setBackgroundResource(R.drawable.ic_fav);
        }
        else
        {
            btnFavourite.setBackgroundResource(R.drawable.ic_fav_border);
        }

        // saving a recipe to favourite
        btnFavourite.setOnClickListener(v-> {
            // now we will save the recipe as a favourite
            if (userId != 0)
            {
                if (isFavourite)
                {
                    long removeFavResult = dbHelper.removeFavourite(userId, recipeId);

                    if (removeFavResult != -1)
                    {
                        btnFavourite.setBackgroundResource(R.drawable.ic_fav_border);
                        Toast.makeText(this, "The recipe was removed from your favourites", Toast.LENGTH_SHORT).show();
                    }
                }

                if (!isFavourite) {
                    long result = dbHelper.saveFavourite(userId, recipeId);

                    if (result != -1) {
                        btnFavourite.setBackgroundResource(R.drawable.ic_fav);
                        Toast.makeText(this, "The recipe was added to your favourites!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            else
            {
                Toast.makeText(this, "The recipe could not be saved as a favourite", Toast.LENGTH_SHORT).show();
            }
        });

        // comments count
        int commentCount = dbHelper.getCommentsCountForRecipe(recipeId);
        if (commentCount > 0)
        {
            tvComments.setText("Comments (" + commentCount + ")");
        }
        else
        {
            tvComments.setText("Comments (0)");
        }

        // setting recycler view of comments
        rvComments = findViewById(R.id.rvComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvComments.setLayoutManager(linearLayoutManager);

        commentList = dbHelper.getAllCommentsForRecipe(recipeId);
        commentAdapter = new CommentAdapter(commentList, context);
        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(commentAdapter);

        // post comment button will only be visible if edit comment field is clicked
        btnPostComment.setVisibility(TextView.GONE);

        etAddComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    btnPostComment.setVisibility(TextView.VISIBLE);
                }
                else
                {
                    btnPostComment.setVisibility(TextView.GONE);
                }
            }
        });

        // adding a comment
        btnPostComment.setOnClickListener(v-> postComment());

    }

    private void postComment()
    {
        String comment = etAddComment.getText().toString();

        // user id
        SharedPreferences sharedPreferences = getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        int userId = dbHelper.getUserId(userEmail);

        // recipe id
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        // profanity filter
        String[] profanityArray = {"damn", "hell", "shit", "fuck", "idiot", "bastard"};
        boolean containsProfanity = false;

        // checking if comments leave any profanity
        for (String badWord : profanityArray)
        {
            if (comment.contains(badWord))
            {
                containsProfanity = true;
                break;
            }
        }

        if (containsProfanity)
        {
            Toast.makeText(this, "Please avoid using any offense language.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // submit comment
            boolean result = dbHelper.postComment(recipeId, userId, comment);

            if (result)
            {
                Toast.makeText(this, "Thanks for submitting your comment!", Toast.LENGTH_SHORT).show();
                commentAdapter.updateData(commentList);
            }
            else
            {
                Toast.makeText(this, "There was something wrong with posting your comment!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}