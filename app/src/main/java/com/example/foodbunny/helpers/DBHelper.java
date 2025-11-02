package com.example.foodbunny.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.foodbunny.objects.Comment;
import com.example.foodbunny.objects.Favourite;
import com.example.foodbunny.objects.Ingredient;
import com.example.foodbunny.objects.Recipe;
import com.example.foodbunny.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    // database
    private static final String DATABASE_NAME = "FoodBunny.db";
    private static final int DATABASE_VERSION = 2;

    // users table
    private static final String TABLE_USERS = "user";
    private static final String USERS_COLUMN_ID = "id";
    private static final String USERS_COLUMN_NAME = "name";
    private static final String USERS_COLUMN_EMAIL = "email";
    private static final String USERS_COLUMN_PASSWORD = "password";

    // food recipes table
    private static final String TABLE_FOOD_RECIPES = "food_recipe";
    private static final String FOOD_RECIPES_COLUMN_ID = "id";
    private static final String FOOD_RECIPES_COLUMN_NAME = "recipe_name";
    private static final String FOOD_RECIPES_COLUMN_IMAGE = "recipe_image";
    private static final String FOOD_RECIPES_COLUMN_CAPTION = "caption";
    private static final String FOOD_RECIPES_COLUMN_CATEGORY = "category";
    private static final String FOOD_RECIPES_COLUMN_TYPE = "type";
    private static final String FOOD_RECIPES_COLUMN_SERVING_SIZE = "serving_size";
    private static final String FOOD_RECIPES_COLUMN_DURATION = "duration";
    private static final String FOOD_RECIPES_COLUMN_USER_ID = "user_id";
    private static final String FOOD_RECIPES_COLUMN_DATE_POSTED = "date_posted";

//    // ingredients tbl
    private static final String TABLE_INGREDIENTS = "ingredient";
    private static final String INGREDIENTS_COLUMN_ID = "id";
    private static final String INGREDIENTS_COLUMN_NAME = "ingredient_name";
    private static final String INGREDIENTS_COLUMN_QTY = "qty";
    private static final String INGREDIENTS_COLUMN_UNIT = "unit";

    // directions tbl
    private static final String TABLE_DIRECTIONS = "direction";
    private static final String DIRECTIONS_COLUMN_ID = "position";
    private static final String DIRECTIONS_COLUMN_INSTRUCTION = "instruction";

    // favourites table
    private static final String TABLE_FAVOURITES = "favourite";
    private static final String FAVOURITES_COLUMN_USER_ID = "user_id";
    private static final String FAVOURITES_COLUMN_RECIPE_ID = "recipe_id";

   // recipe ingredients table
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredient";
    private static final String RECIPE_INGREDIENTS_COLUMN_RECIPE_ID = "recipe_id";
    private static final String RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID = "ingredient_id";

    // recipe directions table
    private static final String TABLE_RECIPE_DIRECTIONS = "recipe_direction";
    private static final String RECIPE_DIRECTIONS_COLUMN_RECIPE_ID = "recipe_id";
    private static final String RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID = "direction_id";

    // comments table
    private static final String TABLE_COMMENTS = "comments";
    private static final String COMMENT_COLUMN_ID = "comment_id";
    private static final String COMMENT_COLUMN_COMMENT = "comment";
    private static final String COMMENT_COLUMN_RECIPE_ID = "recipe_id";
    private static final String COMMENT_COLUMN_USER_ID = "user_id";
    private static final String COMMENT_COLUMN_DATE_POSTED = "date_posted";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    // creating the users table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                USERS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_COLUMN_NAME + " TEXT, " +
                USERS_COLUMN_EMAIL + " TEXT, " +
                USERS_COLUMN_PASSWORD + " TEXT)";
        try {
            db.execSQL(createUserTable);
            Log.d("Database", "User table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createRecipeTable = "CREATE TABLE " + TABLE_FOOD_RECIPES + " (" +
                FOOD_RECIPES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FOOD_RECIPES_COLUMN_NAME + " TEXT, " +
                FOOD_RECIPES_COLUMN_IMAGE + " TEXT, " +
                FOOD_RECIPES_COLUMN_CAPTION + " TEXT, " +
                FOOD_RECIPES_COLUMN_CATEGORY + " TEXT, " +
                FOOD_RECIPES_COLUMN_TYPE + " TEXT, " +
                FOOD_RECIPES_COLUMN_SERVING_SIZE + " TEXT, " +
                FOOD_RECIPES_COLUMN_DURATION + " TEXT, " +
                FOOD_RECIPES_COLUMN_USER_ID + " TEXT, " +
                FOOD_RECIPES_COLUMN_DATE_POSTED + " TEXT " +")" ;
        try {
            db.execSQL(createRecipeTable);
            insertFoodRecipesData(db);
            Log.d("Database", "Recipe table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createIngredientTable = "CREATE TABLE " + TABLE_INGREDIENTS+ " (" +
                INGREDIENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INGREDIENTS_COLUMN_NAME + " TEXT, " +
                INGREDIENTS_COLUMN_UNIT + " TEXT, " +
                INGREDIENTS_COLUMN_QTY + " DOUBLE) " ;
        try {
            db.execSQL(createIngredientTable);
            insertIngredientsData(db);
            Log.d("Database", "Ingredient table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createRecipeIngredientTable = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                RECIPE_INGREDIENTS_COLUMN_RECIPE_ID + " INTEGER, " +
                RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID + " INTEGER, " +
                " FOREIGN KEY(" + RECIPE_INGREDIENTS_COLUMN_RECIPE_ID + ") REFERENCES " + TABLE_FOOD_RECIPES + "(" + FOOD_RECIPES_COLUMN_ID + ") ON DELETE CASCADE, " +
                " FOREIGN KEY(" + RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID + ") REFERENCES " + TABLE_INGREDIENTS + "(" + INGREDIENTS_COLUMN_ID + ") ON DELETE CASCADE, " +
                " PRIMARY KEY(" + RECIPE_INGREDIENTS_COLUMN_RECIPE_ID + ", " + RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID + "))";
        try {
            db.execSQL(createRecipeIngredientTable);
            insertRecipeIngredientsData(db);
            Log.d("Database", "Recipe & Ingredient table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createDirectionTable = "CREATE TABLE " + TABLE_DIRECTIONS + " (" +
                DIRECTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DIRECTIONS_COLUMN_INSTRUCTION + " TEXT) " ;
        try {
            db.execSQL(createDirectionTable);
            insertDirectionsData(db);
            Log.d("Database", "Directions table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createRecipeDirectionsTable = "CREATE TABLE " + TABLE_RECIPE_DIRECTIONS + " (" +
                RECIPE_DIRECTIONS_COLUMN_RECIPE_ID + " INTEGER, " +
                RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID + " INTEGER, " +
                " FOREIGN KEY(" + RECIPE_DIRECTIONS_COLUMN_RECIPE_ID + ") REFERENCES " + TABLE_FOOD_RECIPES + "(" + FOOD_RECIPES_COLUMN_ID + ") ON DELETE CASCADE, " +
                " FOREIGN KEY(" + RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID + ") REFERENCES " + TABLE_DIRECTIONS + "(" + DIRECTIONS_COLUMN_ID + ") ON DELETE CASCADE, " +
                " PRIMARY KEY(" + RECIPE_DIRECTIONS_COLUMN_RECIPE_ID + ", " + RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID + "))";
        try {
            db.execSQL(createRecipeDirectionsTable);
            insertRecipeDirectionsData(db);
            Log.d("Database", "Recipe & Direction table created successfully!");
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error creating table: " + e.getMessage());
        }

        String createFavouriteTable = "CREATE TABLE " + TABLE_FAVOURITES + " (" +
                FAVOURITES_COLUMN_USER_ID + " INTEGER, " +
                FAVOURITES_COLUMN_RECIPE_ID + " INTEGER, " +
                " FOREIGN KEY(" + FAVOURITES_COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USERS_COLUMN_ID + ") ON DELETE CASCADE, " +
                " FOREIGN KEY(" + FAVOURITES_COLUMN_RECIPE_ID + ") REFERENCES " + TABLE_FOOD_RECIPES + "(" + FOOD_RECIPES_COLUMN_ID + ") ON DELETE CASCADE, " +
                " PRIMARY KEY(" + FAVOURITES_COLUMN_USER_ID + ", " + FAVOURITES_COLUMN_RECIPE_ID + "))";
        try {
            db.execSQL(createFavouriteTable);
            Log.d("Database", "Favourite table created successfully!");
        }
        catch(SQLException e) {
            Log.e("Database", "Error creating table: " + e.getMessage());
        }

        String createCommentsTable = "CREATE TABLE " + TABLE_COMMENTS + " (" +
                COMMENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COMMENT_COLUMN_COMMENT + " TEXT, " +
                COMMENT_COLUMN_USER_ID + " INTEGER, " +
                COMMENT_COLUMN_RECIPE_ID + " INTEGER, " +
                COMMENT_COLUMN_DATE_POSTED + " DATE, " +
                " FOREIGN KEY(" + COMMENT_COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USERS_COLUMN_ID + ") ON DELETE CASCADE, " +
                " FOREIGN KEY(" + COMMENT_COLUMN_RECIPE_ID + ") REFERENCES " + TABLE_FOOD_RECIPES + "(" + FOOD_RECIPES_COLUMN_ID + ") ON DELETE CASCADE)";
        try {
            db.execSQL(createCommentsTable);
            Log.d("Database", "Comments table created successfully!");
        }
        catch(SQLException e) {
            Log.e("Database", "Error creating table: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_DIRECTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        onCreate(db);
    }

    private void insertFoodRecipesData(SQLiteDatabase db)
    {
        db.execSQL("INSERT INTO " + TABLE_FOOD_RECIPES + " ( recipe_name,caption, category, type, serving_size, duration, recipe_image, user_id, date_posted) VALUES " +
               "('Corn Salad', 'Healthy, refreshing and filling', 'Snack', 'Healthy', '1', '10 minutes', 'img_corn_salad', NULL, NULL), " +
                "('Potato Pasta', 'Hearty, energetic dish', 'Dinner', 'Healthy', '2', '35 minutes', 'img_potato_pasta', NULL, NULL), " +
                "('Blueberry & Lemon Muffins', 'Blueberry bliss with that tangy lemon bite', 'Dessert', 'Easy to Make', '5', '30 minutes', 'img_blueberrylemon_muffins', NULL, NULL), " +
                "('Watermelon Lassi', 'A twist on the traditional Sweet Lassi', 'Beverage', 'Healthy', '2', '5 minutes', 'img_watermelon_lassi', NULL, NULL), " +
                "('Granola Bar', 'Healthy, refreshing and filling', 'Breakfast', 'Healthy', '1', '10 minutes', 'img_granola_bars', NULL, NULL), " +
                "('Signature Pizza', 'Healthy, refreshing and filling', 'Dinner', 'Vegan', '1', '10 minutes', 'img_pizza', NULL, NULL), " +
                "('Shrimp Pasta', 'Healthy, refreshing and filling', 'Dinner', 'Seafood', '1', '10 minutes', 'img_shrimp_pasta', NULL, NULL), " +
                "('Bread Pudding', 'Healthy, refreshing and filling', 'Dessert', 'Dairy', '1', '10 minutes', 'img_bread_pudding', NULL, NULL), " +
                "('Corn Toast', 'Healthy, refreshing and filling', 'Breakfast', 'Healthy', '1', '10 minutes', 'img_corn_toast', NULL, NULL), " +
                "('Loaded Wrap', 'Healthy, refreshing and filling', 'Dinner', 'Healthy', '1', '10 minutes', 'img_loaded_wrap', NULL, NULL), " +
                "('Chai', 'Healthy, refreshing and filling', 'Beverage', 'Easy to Make', '1', '10 minutes', 'img_chai', NULL, NULL), " +
                "('Butter Cookies', 'Sweet, light, and a perfect dessert', 'Dessert', 'Easy to Make', '3', '30 minutes', 'img_butter_cookies', NULL, NULL), " +
                "('Mango Mojitto', 'Fresh, tropical and refreshing', 'Beverage', 'Healthy', '1', '5 minutes', 'img_mango_mojito', NULL, NULL), " +
                "('Oreo Milkshake', 'Creamy, sweet with that perfect Oreo crunch', 'Beverage', 'Dairy', '1', '8 minutes', 'img_oreo_milkshake', NULL, NULL), " +
                "('Faluda', 'Happy vibes in a cup', 'Beverage', 'Dairy', '2', '15 minutes', 'img_faluda', NULL, NULL), " +
                "('Mini Fruit Tarts', 'Light, sweet, and dreamy', 'Dessert', 'Vegan', '5', '45 minutes', 'img_fruit_tarts', NULL, NULL), " +
                "('Mini Chocolate Tarts', 'Mini but rich and chocolatey', 'Dessert', 'Dairy', '2', '30 minutes', 'img_choc_tarts', NULL, NULL), " +
                "('Cheesecake Tarts', 'Yummy, creamy and topped with berries', 'Dessert', 'Dairy', '10', '2 hours', 'img_cheesecake_tarts', NULL, NULL);"
        );
    }

    private void insertIngredientsData(SQLiteDatabase db)
    {
        db.execSQL("INSERT INTO " + TABLE_INGREDIENTS + " ( ingredient_name, unit, qty ) VALUES " +
                "('Corn', 'Cup', 1), " + "('Zucchini', 'Cup', 1), " + "('Lime', NULL, 1), " +
                "('Red Onion', 'Finely minced', 0.5), " +  "('Vinegar Oil', 'Tbsp', 4), " +  "('Chili Pepper', NULL, 1), " + "('Tomato Paste', 'Tbsp', 1), " + "('Potatoes', NULL, 2), " + "('Lightly salted water', 'ml', 500), " + "('Parmigiano-Reggiano', NULL, 1), " + "('Cavatelli', 'g', 150 ), " + "('grated Parmigiano Reggiano', 'Tbsp', 2), " +
                "('Unsalted butter', 'g', 100), " + "('Icing sugar', 'g', 125 ), " + "('Flour', 'g', 25), " + "('Almonds', 'g', 85), " + "('Blueberries', 'g', 85)," + "('Eggs', NULL, 3), " + "('Lemon', NULL, 1), " +
                "('Watermelon', 'cup', 1.5), " + "('Yoghurt', 'cup', 1/2), " + "('Agave', 'Tbsp', 3), " + "('Cardamom', 'pods', 3), " + "('Cumin-coriander powder', 'Tsp', 1/8), " + "('Pistachios', 'Tbsp', 1);"
    );
    }
    private void insertRecipeIngredientsData(SQLiteDatabase db)

    {
        db.execSQL("INSERT INTO " + TABLE_RECIPE_INGREDIENTS + " ( recipe_id, ingredient_id ) VALUES " +
                "(1, 1), " + "(1, 2), " + "(1, 3), " +
                "(2, 4), " + "(2, 5), " + "(2, 6), " + "(2, 7), " + "(2, 8), " + "(2, 9), " + "(2, 10), " + "(2, 11), " + "(2, 12), " +
                "(3, 13), " + "(3, 14), " + "(3, 15), " + "(3, 16), " + "(3, 17), " + "(3, 18), " + "(3, 19), " +
                "(4, 20), " + "(4, 21), " + "(4, 22), " + "(4, 23), " + "(4, 24), " + "(4, 25);"
        );
    }

    private void insertDirectionsData(SQLiteDatabase db)
    {
        db.execSQL("INSERT INTO " + TABLE_DIRECTIONS + " ( position,instruction ) VALUES " +
                "(1, 'Boil the corn;\n" +
                "Remove it from the cob;\n" +
                "Chop the zucchini;\n" +
                "Mix the corn, zucchini with the greek yogurt;\n" +
                "Combine with spices of your choice;')," +

                "(2, 'Pour the olive oil into a medium pot over low heat. Add the finely minced onion, a pinch of chili pepper and a generous pinch of salt, then cook until soft, stirring occasionally, about 8 minutes;\n\n" +
                "Add the tomato paste, stir, and cook until caramelized, about one minute. It will give a pinkish and inviting colour to your pasta and potatoes;\n\n" +
                "Add the diced potatoes, stir, and cook on low heat for about five minutes, taking care not to brown the potatoes;\n\n" +
                "Pour in 1⅔ cups/400 ml hot water (it should be enough to cover the potatoes), add the Parmigiano-Reggiano rind, and cook on a medium flame for about 20 minutes, stirring occasionally, until you can easily mash the potatoes;\n\n" +
                "Pour in the remaining hot water, and the pasta, and stir thoroughly. Simmer until the pasta is al dente, about 10 minutes;\n\n" +
                "When the pasta is cooked through, and the soup is thick, remove the pot from the heat, add the grated Parmigiano Reggiano, and stir energetically. Taste and adjust the seasoning with salt;\n\n" +
                "Ladle the soup into warmed bowls, drizzle each serving with some olive oil, sprinkle with freshly ground black pepper, and serve. The Parmigiano-Reggiano rind can be cut into smaller pieces and added to the bowls;'), " +

                "(3, 'Preheat the oven to fan 180C;\n\n" +
                "Generously butter six non-stick friand or muffin tins;\n\n" +
                "Melt the butter and set aside to cool;\n\n" +
                "Sift the icing sugar and flour into a bowl. Add the almonds and mix everything;\n\n" +
                "Whisk the egg whites in another bowl until they form a light, floppy foam;\n\n" +
                "Make a well in the centre of the dry ingredients, tip in the egg whites and lemon rind, then lightly stir in the butter to form a soft batter;\n\n" +
                "Divide the batter among the tins, a large serving spoon is perfect for this job;\n\n" +
                "Sprinkle a handful of blueberries over each cake and bake for 15-20 minutes until just firm to the touch and golden brown;\n\n" +
                "Cool in the tins for 5 minutes, then turn out and cool on a wire rack;\n\n" +
                "To serve, dust lightly with icing sugar;'), " +

                "(4, 'Throw all the ingredients in a blender;\n" +
                "Blend on low speed until all is blended well;\n" +
                "Top with finely crushed pistachios;\n" +
                "Serve Immediately;');"
        );
    }

    private void insertRecipeDirectionsData(SQLiteDatabase db)
    {
        db.execSQL("INSERT INTO " + TABLE_RECIPE_DIRECTIONS + " ( recipe_id, direction_id ) VALUES " +
                "(1, 1), " +
                "(2, 2), " +
                "(3, 3), " +
                "(4, 4);"
        );
    }

    // registering the user
    public boolean registerUser(String name, String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_NAME, name);
        values.put(USERS_COLUMN_EMAIL, email);
        values.put(USERS_COLUMN_PASSWORD, password);

        Log.d("DBHelper", "Adding user with email: " + email);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // checking user before login
    public boolean checkUser(String email, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?",
                new String[]{email, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // getting username
    public String getUserName(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String name = null;
        Cursor cursor = db.rawQuery("SELECT name FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME));
            cursor.close();
        }

        Log.d("DBHelper", "Username of email: " + email + " is " + name);

        db.close();
        return name;
    }

    // getting user profile details
    public User getUserProfile(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name, email, password FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        if (cursor != null && cursor.moveToFirst()) {
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_PASSWORD));

            cursor.close();
            return new User(userName, userEmail, userPassword);
        }
            cursor.close();

        return null;
    }

    // updating user profile details
    public boolean updateUserProfile(String email, String name, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_COLUMN_NAME, name);
        values.put(USERS_COLUMN_EMAIL, email);
        values.put(USERS_COLUMN_PASSWORD, password);

        int rowsAffected = db.update("user", values, "email=?", new String[]{email});
        return rowsAffected > 0;
    }

    // adding food recipe
    public long addRecipe(String name, String caption, String category, String type, String duration, int servingSize, String recipeImage, int userId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FOOD_RECIPES_COLUMN_NAME, name);
        values.put(FOOD_RECIPES_COLUMN_CAPTION, caption);
        values.put(FOOD_RECIPES_COLUMN_CATEGORY, category);
        values.put(FOOD_RECIPES_COLUMN_TYPE, type);
        values.put(FOOD_RECIPES_COLUMN_DURATION, duration);
        values.put(FOOD_RECIPES_COLUMN_SERVING_SIZE, servingSize);
        values.put(FOOD_RECIPES_COLUMN_IMAGE, recipeImage);
        values.put(FOOD_RECIPES_COLUMN_USER_ID, userId);

        String datePosted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(FOOD_RECIPES_COLUMN_DATE_POSTED, datePosted);

        Log.d("DBHelper", "Adding recipe: " + name);

        long result = db.insert(TABLE_FOOD_RECIPES, null, values);
        db.close();
        return result;
    }

    // adding the ingredients of a recipe
    public long addIngredientsToARecipe(long recipeId, String ingredientName, String unit, String qty)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // add the ingredient first
        ContentValues ingValues = new ContentValues();
        ingValues.put(INGREDIENTS_COLUMN_NAME, ingredientName);
        ingValues.put(INGREDIENTS_COLUMN_UNIT, unit);
        ingValues.put(INGREDIENTS_COLUMN_QTY, qty);
        long ingId = db.insert(TABLE_INGREDIENTS, null, ingValues);

        // then link it in the ingredients recipe table
        ContentValues ingRecipeValues = new ContentValues();
        ingRecipeValues.put(RECIPE_INGREDIENTS_COLUMN_RECIPE_ID, recipeId);
        ingRecipeValues.put(RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID, ingId);
        long result = db.insert(TABLE_RECIPE_INGREDIENTS, null, ingRecipeValues);

        db.close();
        return result;
    }

    // adding the directions of a recipe
    public long addDirectionsToARecipe(long recipeId, String instruction)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        // add the ingredient first
        ContentValues directionValues = new ContentValues();
        directionValues.put(DIRECTIONS_COLUMN_INSTRUCTION, instruction);
        long directionId = db.insert(TABLE_DIRECTIONS, null, directionValues);

        // then link it in the ingredients recipe table
        ContentValues directionRecipeValues = new ContentValues();
        directionRecipeValues.put(RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID, directionId);
        directionRecipeValues.put(RECIPE_DIRECTIONS_COLUMN_RECIPE_ID, recipeId);
        long result = db.insert(TABLE_RECIPE_DIRECTIONS, null, directionRecipeValues);

        db.close();
        return result;
    }

    // get recipe all details
    public Recipe getRecipeDetails(int recipeId){
        SQLiteDatabase db = this.getReadableDatabase();
        Recipe recipe = null;

        Cursor cursor = db.rawQuery(
                "SELECT fr.*, i." + INGREDIENTS_COLUMN_NAME + ", i." + INGREDIENTS_COLUMN_UNIT +  ", i." + INGREDIENTS_COLUMN_QTY +
                        " FROM " + TABLE_FOOD_RECIPES + " fr " +
                        " INNER JOIN " + TABLE_RECIPE_INGREDIENTS + " ri " +
                        " ON fr." + FOOD_RECIPES_COLUMN_ID + " = ri." + RECIPE_INGREDIENTS_COLUMN_RECIPE_ID +
                        " INNER JOIN " + TABLE_INGREDIENTS + " i " +
                        " ON ri." + RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID + " = i." + INGREDIENTS_COLUMN_ID +
                        " WHERE fr." + FOOD_RECIPES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(recipeId)}
        );

        if (cursor.moveToFirst())
        {
            recipe = new Recipe();
            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_ID)));
            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_NAME)));
            recipe.setCaption(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_CAPTION)));
            recipe.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_CATEGORY)));
            recipe.setType(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_TYPE)));
            recipe.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_DURATION)));
            recipe.setRecipeImage(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_IMAGE)));
            recipe.setServingSize(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_SERVING_SIZE)));

            List<Ingredient> ingredients = new ArrayList<>();

            do
            {
                Ingredient ing = new Ingredient();
                ing.setUnit(cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENTS_COLUMN_UNIT)));
                ing.setName(cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENTS_COLUMN_NAME)));
                ing.setQty(cursor.getInt(cursor.getColumnIndexOrThrow(INGREDIENTS_COLUMN_QTY)));

                ingredients.add(ing);
            }
            while (cursor.moveToNext());

            recipe.setIngredients(ingredients);
        }

        // getting directions now
        if (recipe != null)
        {
            List<String> directions = new ArrayList<>();
            Cursor directionCursor = db.rawQuery("SELECT d." + DIRECTIONS_COLUMN_INSTRUCTION +
                            " FROM " + TABLE_DIRECTIONS + " d " +
                            " INNER JOIN " + TABLE_RECIPE_DIRECTIONS + " rd " +
                            " ON d." + DIRECTIONS_COLUMN_ID + " = rd." + RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID +
                            " WHERE rd." + RECIPE_DIRECTIONS_COLUMN_RECIPE_ID + " = ?",
                    new String[]{String.valueOf(recipeId)});

            if (directionCursor.moveToFirst()) {
                do
                {
                   String instruction= directionCursor.getString(directionCursor.getColumnIndexOrThrow(DIRECTIONS_COLUMN_INSTRUCTION));

                 if (instruction.contains(";"))
                 {
                       directions.addAll(Arrays.asList(instruction.split(";")));
                 }
                 else
                 {
                   directions.add(instruction);
                 }

                } while (directionCursor.moveToNext());
            }
            directionCursor.close();
            recipe.setDirections(directions);
        }

        db.close();
        return recipe;
    }

    // getting filtered recipes
    public List<Recipe> getFilteredRecipes(String category, String type, String searchQuery)
    {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // base query with select function
        String baseQuery = "SELECT " + FOOD_RECIPES_COLUMN_ID + "," +
                FOOD_RECIPES_COLUMN_NAME + ","
                + FOOD_RECIPES_COLUMN_IMAGE +
                " FROM " + TABLE_FOOD_RECIPES + " WHERE 1=1";

        // filters based on search / category / type
        List<String> args = new ArrayList<>();

        if (category != null && !category.isEmpty()) {
            baseQuery += " AND " + FOOD_RECIPES_COLUMN_CATEGORY + " = ?";
            args.add(category);
        }

        if (type != null && !type.isEmpty()) {
            baseQuery += " AND " + FOOD_RECIPES_COLUMN_TYPE + " = ?";
            args.add(type);
        }

        if (searchQuery != null && !searchQuery.isEmpty()) {
            baseQuery += " AND " + FOOD_RECIPES_COLUMN_NAME + " LIKE ?";
            args.add("%" + searchQuery + "%");
        }

        Cursor cursor = db.rawQuery(baseQuery, args.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_NAME)));
                recipe.setRecipeImage(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_IMAGE)));

                recipeList.add(recipe);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        Log.d("DBHelper", "Fetched recipes: " + recipeList.size());
        return recipeList ;
    }

//    // getting recipe info through recipe name
//    public Recipe getRecipeByName(String name) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOOD_RECIPES + " WHERE recipe_name LIKE ?", new String[]{"%" + name + "%"});
//        Recipe recipe = null;
//        if (cursor.moveToFirst()) {
//            recipe = new Recipe();
//            recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_ID)));
//            recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_NAME)));
//            recipe.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_DURATION)));
//            recipe.setServingSize(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_SERVING_SIZE)));
//        }
//        cursor.close();
//        db.close();
//        return recipe;
//    }

    // get user id through user email
    public Integer getUserId(String email)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = 0;
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});

        if (cursor != null && cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_COLUMN_ID));
            cursor.close();
        }

        Log.d("DBHelper", "User Id of email: " + email + " is " + id);

        db.close();
        return id;
    }

    // checking if recipe is already a favourite
    public boolean checkIfFav(int userId, int recipeId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVOURITES + " WHERE user_id = ? AND recipe_id = ?",
                new String[]{String.valueOf(userId), String.valueOf(recipeId)});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // saving a recipe as a favourite
    public long saveFavourite(int userId, int recipeId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVOURITES_COLUMN_USER_ID, userId);
        values.put(FAVOURITES_COLUMN_RECIPE_ID, recipeId);
        Log.d("DBHelper", "The recipe Id: " + recipeId + " was saved as a favourite for User Id: " +userId);

        long result = db.insert(TABLE_FAVOURITES, null, values);
        return result;
    }

    // removing a recipe from fav
    public int removeFavourite(int userId, int recipeId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FAVOURITES, FAVOURITES_COLUMN_USER_ID + " = ? AND " + FAVOURITES_COLUMN_RECIPE_ID + " = ?",
                 new String[]{String.valueOf(userId), String.valueOf(recipeId)});
        db.close();
        return result;
    }

    // getting all favourites
    public List<Favourite> getAllMyFavourites(int userId)
    {
        List<Favourite> favList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT r." + FOOD_RECIPES_COLUMN_NAME + ", " +
                "r." + FOOD_RECIPES_COLUMN_IMAGE + ", " +
                "r." + FOOD_RECIPES_COLUMN_ID + ", " +
                "f." + FAVOURITES_COLUMN_RECIPE_ID +
                " FROM " + TABLE_FAVOURITES + " f " +
                "JOIN " + TABLE_FOOD_RECIPES + " r " +
                "ON f." + FAVOURITES_COLUMN_RECIPE_ID + " = r." + FOOD_RECIPES_COLUMN_ID +
                " WHERE f." + FAVOURITES_COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst())
        {
            do
            {
                // recipe name and image
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_NAME)));
                recipe.setRecipeImage(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_IMAGE)));

                // recipe id
                Favourite favourite = new Favourite();
                favourite.setRecipeId(cursor.getInt(cursor.getColumnIndexOrThrow(FAVOURITES_COLUMN_RECIPE_ID)));
                favourite.setRecipe(recipe);
                favList.add(favourite);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return favList;
    }

    // getting all my recipes
    public List<Recipe> getAllMyRecipes(int userId)
    {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + FOOD_RECIPES_COLUMN_NAME + ", " +
                 FOOD_RECIPES_COLUMN_IMAGE + ", " +
                 FOOD_RECIPES_COLUMN_ID + ", " +
                 FOOD_RECIPES_COLUMN_DATE_POSTED +
                " FROM " + TABLE_FOOD_RECIPES +
                " WHERE " + FOOD_RECIPES_COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst())
        {
            do
            {
                // recipe name and image
                Recipe recipe = new Recipe();
                recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_NAME)));
                recipe.setRecipeImage(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_IMAGE)));
                recipe.setDatePosted(cursor.getString(cursor.getColumnIndexOrThrow(FOOD_RECIPES_COLUMN_DATE_POSTED)));

                recipeList.add(recipe);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipeList;
    }

    // deleting a recipe
    public int deleteRecipe(int recipeId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FOOD_RECIPES, FOOD_RECIPES_COLUMN_ID + " = ? ",
                new String[]{String.valueOf(recipeId)});
        db.close();
        return result;
    }

    // updating a recipe information
    public boolean updateRecipe(int recipeId, String name, String caption, String category, String type, String duration, int servingSize, String recipeImage)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FOOD_RECIPES_COLUMN_NAME, name);
        values.put(FOOD_RECIPES_COLUMN_CAPTION, caption);
        values.put(FOOD_RECIPES_COLUMN_CATEGORY, category);
        values.put(FOOD_RECIPES_COLUMN_TYPE, type);
        values.put(FOOD_RECIPES_COLUMN_DURATION, duration);
        values.put(FOOD_RECIPES_COLUMN_SERVING_SIZE, servingSize);
        values.put(FOOD_RECIPES_COLUMN_IMAGE, recipeImage);

        String dateUpdated = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(FOOD_RECIPES_COLUMN_DATE_POSTED, dateUpdated);

        Log.d("DBHelper", "Updating recipe: " + name);

        int rowsAffected = db.update(TABLE_FOOD_RECIPES,  values, "id=?", new String[]{String.valueOf(recipeId)});
        return rowsAffected > 0;
    }

    // updating the ingredients of a recipe
    public boolean updateIngredientsOfARecipe(int recipeId, String ingredientName, String unit, String qty)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        // deleting all existing links for this recipe
        db.delete(TABLE_RECIPE_INGREDIENTS, "recipe_id=?", new String[]{String.valueOf(recipeId)});

        // delete old ingredients if they’re not shared
        db.delete(TABLE_INGREDIENTS, "id IN (SELECT ingredient_id FROM recipe_ingredient WHERE recipe_id=?)", new String[]{String.valueOf(recipeId)});

        try
        {
            // update the ingredient first
            ContentValues ingValues = new ContentValues();
            ingValues.put(INGREDIENTS_COLUMN_NAME, ingredientName);
            ingValues.put(INGREDIENTS_COLUMN_UNIT, unit);
            ingValues.put(INGREDIENTS_COLUMN_QTY, qty);
            long ingId = db.insert(TABLE_INGREDIENTS, null, ingValues);

            // then link it in the ingredients recipe table
            ContentValues ingRecipeValues = new ContentValues();
            ingRecipeValues.put(RECIPE_INGREDIENTS_COLUMN_RECIPE_ID, recipeId);
            ingRecipeValues.put(RECIPE_INGREDIENTS_COLUMN_INGREDIENT_ID, ingId);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingRecipeValues);

            db.setTransactionSuccessful();
            return true;
        }
     catch (Exception e) {
        Log.d("DBHelper", "The error is " + e);
        return false;
    } finally {
        db.endTransaction();
        db.close();
    }
    }

    // updating the directions of a recipe
    public boolean updateDirectionsOfARecipe(long recipeId, String instruction)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        // deleting all existing links for this recipe
        db.delete(TABLE_RECIPE_DIRECTIONS, "recipe_id=?", new String[]{String.valueOf(recipeId)});

        // delete old directions if they’re not shared
        db.delete(TABLE_DIRECTIONS, "position IN (SELECT direction_id FROM recipe_direction WHERE recipe_id=?)", new String[]{String.valueOf(recipeId)});

        try {
            // update the directions first
            ContentValues directionValues = new ContentValues();
            directionValues.put(DIRECTIONS_COLUMN_INSTRUCTION, instruction);
            long directionId = db.insert(TABLE_DIRECTIONS, null, directionValues);

            // then link it in the directions recipe table
            ContentValues directionRecipeValues = new ContentValues();
            directionRecipeValues.put(RECIPE_DIRECTIONS_COLUMN_DIRECTION_ID, directionId);
            directionRecipeValues.put(RECIPE_DIRECTIONS_COLUMN_RECIPE_ID, recipeId);
            db.insert(TABLE_RECIPE_DIRECTIONS, null, directionRecipeValues);

            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception e) {
            Log.d("DBHelper", "The error is " + e);
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    // get comments count for a recipe
    public int getCommentsCountForRecipe(int recipeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) AS total_comments FROM " + TABLE_COMMENTS + " WHERE recipe_id = ?", new String[]{String.valueOf(recipeId)});

        if (cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndexOrThrow("total_comments"));
        }

        cursor.close();
        db.close();

        Log.d("DBHelper", "The comments count for recipe Id " + recipeId + " is " + count);
        return count;
    }


    // get all comments for a recipe
    public List<Comment> getAllCommentsForRecipe(int recipeId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Comment> commentList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT c.*, u." + USERS_COLUMN_NAME +
                " FROM " + TABLE_COMMENTS + " c " +
                "JOIN " + TABLE_USERS + " u " +
                "ON c." + COMMENT_COLUMN_USER_ID + " =u." + USERS_COLUMN_ID +
                " WHERE c.recipe_id = ?", new String[]{String.valueOf(recipeId)});

        if (cursor.moveToFirst()) {
            do {
                Comment comment = new Comment();
                comment.setComment(cursor.getString(cursor.getColumnIndexOrThrow(COMMENT_COLUMN_COMMENT)));
                comment.setDatePosted(cursor.getString(cursor.getColumnIndexOrThrow(COMMENT_COLUMN_DATE_POSTED)));

                User user = new User();
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(USERS_COLUMN_NAME)));

                comment.setUser(user);
                commentList.add(comment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return commentList;
    }

    // post a comment
    public boolean postComment(int recipeId, int userId, String comment)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMMENT_COLUMN_RECIPE_ID, recipeId);
        values.put(COMMENT_COLUMN_USER_ID, userId);
        values.put(COMMENT_COLUMN_COMMENT, comment);

        // getting the date posted
        String datePosted = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        values.put(COMMENT_COLUMN_DATE_POSTED, datePosted);

        long result = db.insert(TABLE_COMMENTS, null, values);
        return result != -1;
    }

}
