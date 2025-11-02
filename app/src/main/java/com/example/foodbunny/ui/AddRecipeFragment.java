package com.example.foodbunny.ui;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodbunny.helpers.DBHelper;
import com.example.foodbunny.R;
import com.example.foodbunny.objects.Ingredient;
import com.example.foodbunny.objects.Recipe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddRecipeFragment extends Fragment {

    private EditText etRecipeName, etCaption, etServings, etPrepTime, etIngredients, etDirections;
    private Spinner spnCategory,spnType;
    private ImageView imgAddImg, imgAddImgIcon;
    private Button btnUpload;
    private DBHelper dbHelper;
    private static final int PERMISSION_REQUEST_READ = 200;
    private String savedImageFileName;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);

        dbHelper = new DBHelper(requireActivity());
        etRecipeName = view.findViewById(R.id.etRecipeName);
        etCaption = view.findViewById(R.id.etCaption);
        etServings = view.findViewById(R.id.etServings);
        etPrepTime = view.findViewById(R.id.etPrepTime);
        etIngredients = view.findViewById(R.id.etIngredients);
        etDirections = view.findViewById(R.id.etDirections);
        btnUpload = view.findViewById(R.id.btnUpload);
        imgAddImg = view.findViewById(R.id.imgAddImg);
        imgAddImgIcon = view.findViewById(R.id.imgAddImgIcon);
        spnCategory = view.findViewById(R.id.spnCategory);
        spnType = view.findViewById(R.id.spnType);

        // register launcher
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null) {
                            // setting the image selected
                            imgAddImg.setImageURI(imageUri);
                            imgAddImgIcon.setVisibility(View.GONE);

                            // saving in internal storage
                            new Thread(() -> {
                                String fileName = saveUriToInternalStorage(imageUri);
                                if (fileName != null) {
                                    savedImageFileName = fileName;
                                    Log.d("AddRecipeFragment", "Saved image filename: " + savedImageFileName);
                                } else {
                                    Log.e("AddRecipeFragment", "Failed to save image");
                                }
                            }).start();
                        }
                    }
                });

        // programming the ingredients edit text
        etIngredients.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && s.charAt(0) != '•')
                {
                    etIngredients.setText("•");
                    etIngredients.setSelection(etIngredients.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etIngredients.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
            {
                Editable ingText = etIngredients.getText();
                int cursorPosition = etIngredients.getSelectionStart();
                ingText.insert(cursorPosition, "\n•");

                int newPosition = Math.min(cursorPosition + 3, ingText.length());
                etIngredients.setSelection(newPosition);  // 3 because \n• is 3 positions

                return true;
            }
            return false;
        });

        // programming the directions edit text
       etDirections.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1)
                {
                    etDirections.setText("1.");
                    etDirections.setSelection(etDirections.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etDirections.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
            {
                Editable dirText = etDirections.getText();
                int cursorPosition = etDirections.getSelectionStart();

                // counting the number of lines
                String textLine = dirText.toString();
                int lineCount = textLine.split("\n").length;

                // insert next num for every new line
                String newLine = "\n" + (lineCount + 1) + ".";
                dirText.insert(cursorPosition, newLine);

                int newPosition = Math.min(cursorPosition + newLine.length(), dirText.length());
                etDirections.setSelection(newPosition);
                return true;
            }
            return false;
        });
        
        imgAddImg.setOnClickListener(v-> openGallery());

        btnUpload.setOnClickListener(v-> uploadRecipe());

        // checking if a recipe with the recipe id already exists
        int recipeId = requireActivity().getIntent().getIntExtra("recipe_id", -1);
        Recipe existingRecipe = dbHelper.getRecipeDetails(recipeId);

        // if a recipe with that id exists, then the data  be populated
        if (existingRecipe != null)
        {
            // getting the drawable image's name
            // or the image's path
            // depends on how it is stored in the database

            String imagePath = existingRecipe.getRecipeImage();
            if (imagePath != null)
            {
                if (imagePath.startsWith("images"))
                {
                    File file = new File(requireContext().getFilesDir(), imagePath);
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                        imgAddImg.setImageBitmap(bitmap);
                    }
                }
                else
                {
                    int imageResId = getResources().getIdentifier(existingRecipe.getRecipeImage(), "drawable", requireContext().getPackageName());
                    if (imageResId != 0)
                    {
                        imgAddImg.setImageResource(imageResId);
                    }
                }
            }
            else
            {
                imgAddImg.setImageResource(R.drawable.ic_confused_bunny);
            }

            etRecipeName.setText(existingRecipe.getName());
            etCaption.setText(existingRecipe.getCaption());
            etPrepTime.setText(existingRecipe.getDuration());

            // if serving size is for one, it will be 'person'. if more than one person, it will be 'people'
            if (existingRecipe.getServingSize() == 1)
            {
                etServings.setText(existingRecipe.getServingSize() + " PERSON ");
            }
            else
            {
                etServings.setText(existingRecipe.getServingSize() + " PEOPLE ");
            }

            // now getting ingredients
            StringBuilder ingText = new StringBuilder();
            for (Ingredient ing : existingRecipe.getIngredients()) {

                String qtyFormatted = (ing.getQty() % 1 == 0)
                        ? String.valueOf((int) ing.getQty())
                        : String.valueOf(ing.getQty());

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
            etIngredients.setText(ingText.toString());

            // now directions
            if (existingRecipe.getDirections() != null && !existingRecipe.getDirections().isEmpty())
            {
                List<String> directions = existingRecipe.getDirections();
                StringBuilder dirFormatted = new StringBuilder();

                for (int i = 0; i< directions.size() ; i++)
                {
                    dirFormatted.append(i + 1)
                            .append(".")
                            .append(directions.get(i).trim())
                            .append("\n");
                }
                etDirections.setText(dirFormatted.toString().trim());
            }
        }

        return view;
    }

    // opening phone gallaery
    private void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(requireActivity(), "Permission is required to select images", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // saving the uri of the image to the storage
    private String saveUriToInternalStorage(Uri uri) {
        InputStream in = null;
        OutputStream out = null;
        try {
            // get mime type & extension
            // diff extenstions like png, jpg / webp
            String mime = requireContext().getContentResolver().getType(uri);
            String ext = ".jpg";
            if (mime != null) {
                if (mime.equals("image/png")) ext = ".png";
                else if (mime.equals("image/webp")) ext = ".webp";
                else if (mime.contains("/")) {
                    String[] parts = mime.split("/");
                    ext = "." + parts[1];
                }
            }

            // assinging a unique name for the image , but it will start with recipe
            String uniqueName = "recipe_" + UUID.randomUUID().toString() + ext;

            // images directory
            File imagesDir = new File(requireContext().getFilesDir(), "images");
            if (!imagesDir.exists()) imagesDir.mkdirs();

            File outFile = new File(imagesDir, uniqueName);

            in = requireContext().getContentResolver().openInputStream(uri);
            if (in == null) return null;
            out = new FileOutputStream(outFile);

            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();

            return "images/" + uniqueName; // storing relative path of image in database
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try { if (in != null) in.close(); } catch (Exception ignored) {}
            try { if (out != null) out.close(); } catch (Exception ignored) {}
        }
    }

    private void uploadRecipe()
    {
        String recipeName = etRecipeName.getText().toString();
        String caption = etCaption.getText().toString();
        String prepTime = etPrepTime.getText().toString();
        String category = spnCategory.getSelectedItem().toString();
        String type = spnType.getSelectedItem().toString();
        String ingredients = etIngredients.getText().toString();
        String directions = etDirections.getText().toString();

        // getting value of serving size as an integer
        String servings = etServings.getText().toString();

        if (servings.contains("PERSON") || servings.contains("PEOPLE"))
        {
            Toast.makeText(requireActivity(), "Please remove person/people from the serving size field", Toast.LENGTH_SHORT).show();
            return;
        }

        int servingSize = Integer.parseInt(servings);

        // getting user is through user email from shared prefs
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_email", Context.MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("email", "");
        int userId = dbHelper.getUserId(userEmail);
        Log.d("AddRecipeFragment", "The user Id is: " +userId);

        // getting recipe id if it already exists
        int recipeId = requireActivity().getIntent().getIntExtra("recipe_id", -1);

        // validations
        if (recipeName.isEmpty() || caption.isEmpty() || servings.isEmpty() || prepTime.isEmpty() || ingredients.isEmpty() || directions.isEmpty())
        {
            Toast.makeText(requireActivity(), "Please fill in all the details.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (savedImageFileName.isEmpty())
        {
            Toast.makeText(requireActivity(), "Please add a picture of your recipe.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (caption.length() > 75)
        {
            Toast.makeText(requireActivity(), "Your caption is too long. Please shorten it.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!prepTime.toUpperCase().contains("MINUTES") && !prepTime.toUpperCase().contains("HOURS"))
        {
            Toast.makeText(requireActivity(), "Prep time must be in minutes or hours only.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!prepTime.toUpperCase().matches("^\\d+\\s*(MINUTES|HOURS)$")) {
            Toast.makeText(requireActivity(), "Prep time must be a number followed by Minutes or Hours.", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe existingRecipe = dbHelper.getRecipeDetails(recipeId);

        // if a recipe doesn't exist already, the new one will be added
        if (existingRecipe == null)
        {
            long result = dbHelper.addRecipe(recipeName, caption, category, "Overnight", prepTime, servingSize, savedImageFileName, userId);
            Log.d("AddRecipeFragment", "Was the recipe uploaded? Food Recipe Id : " + result);

            if (result != 1)
            {
                // splitting the ingredients by name, unit, qty
                String[] lines = ingredients.split("\n");
                for (String line : lines) {
                    if (line.trim().isEmpty()) continue;
                    // removing the bullet points
                    line = line.replace("•", "").trim();

                    String[] parts = line.split("\\s+");
                    String qty = "";
                    String unit = "";
                    String name = "";

                    // as long as the ingredient line has >=3 parts, then the array will be split
                    if (parts.length >= 3) {
                        qty = parts[0];
                        unit = parts[1];
                        name = TextUtils.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                    } else if (parts.length == 2) {
                        qty = parts[0];
                        name = parts[1];
                    } else if (parts.length == 1) {
                        name = parts[0];
                        qty = "1.0";
                    }

                    // adding every ingredient to the database
                    dbHelper.addIngredientsToARecipe(result, name, unit, qty);
                }

                // adding directions
                // removing the numbers from the directions
                String formattedDirections = directions.replaceAll("^\\\\d+\\\\.\\\\s*", "");
                formattedDirections = formattedDirections.replaceAll("(?m)^\\d+\\.\\s*", ""); // removes from every line

                // splitting it by every line break
                String[] directionsList = formattedDirections.split("\n");
                String direction = String.join(";", directionsList);
                Log.d("AddRecipeFragment", "The directions as one string: " + direction);

                long dirResult = dbHelper.addDirectionsToARecipe(result, direction);

                if (dirResult != -1)
                {
                    Toast.makeText(requireActivity(), "Your recipe was successfully uploaded! Thank you for your recipe!", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(requireActivity(), "There was an error with uploading your recipe.!", Toast.LENGTH_SHORT).show();
            }
        }

        // but if a recipe already exists, then the recipe info will only be updated
        else
        {
            dbHelper.updateRecipe(recipeId, recipeName, caption, category, type, prepTime, servingSize, savedImageFileName);

            // ingredients and directions has to be updated

            // ingredients first
            // splitting the ingredients by name, unit, qty
            String[] lines = ingredients.split("\n");
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;
                // removing the bullet points
                line = line.replace("•", "").trim();

                String[] parts = line.split("\\s+");
                String qty = "";
                String unit = "";
                String name = "";

                // as long as the ingredient line has >=3 parts, then the array will be split
                if (parts.length >= 3) {
                    qty = parts[0];
                    unit = parts[1];
                    name = TextUtils.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
                } else if (parts.length == 2) {
                    qty = parts[0];
                    name = parts[1];
                } else if (parts.length == 1) {
                    name = parts[0];
                    qty = "1.0";
                }

                // adding every ingredient to the database
                dbHelper.updateIngredientsOfARecipe(recipeId, name, unit, qty);
            }

            // then directions
            // removing the numbers from the directions
            String formattedDirections = directions.replaceAll("^\\\\d+\\\\.\\\\s*", "");
            formattedDirections = formattedDirections.replaceAll("(?m)^\\d+\\.\\s*", ""); // removes from every line

            // splitting it by every line break
            String[] directionsList = formattedDirections.split("\n");
            String direction = String.join(";", directionsList);
            Log.d("AddRecipeFragment", "The directions as one string: " + direction);

            boolean dirResult = dbHelper.updateDirectionsOfARecipe(recipeId, direction);

            if (dirResult)
            {
                Toast.makeText(requireActivity(), "Your recipe was successfully updated! Thank you for your recipe!", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireActivity(), "Something went wrong with updating your recipe.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}