package com.example.foodbunny.objects;

import java.util.List;

public class Favourite {
    private int userId;
    private int recipeId;
    private Recipe recipe;

    public Favourite() {
    }

    public Favourite(int userId, int recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public Favourite(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
