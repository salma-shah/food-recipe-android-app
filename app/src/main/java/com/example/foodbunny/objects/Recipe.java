package com.example.foodbunny.objects;

import java.util.Date;
import java.util.List;

public class Recipe {
    private int id;
    private String name, caption, category, type, duration, recipeImage, datePosted;
    private int servingSize;
    private List<Ingredient> ingredients;
    private List<String> directions;

//    public Recipe(int id, String name, String caption, String category, String type, String duration, int servingSize, String recipeImage) {
//        this.id = id;
//        this.name = name;
//        this.caption = caption;
//        this.category = category;
//        this.type = type;
//        this.duration = duration;
//        this.servingSize = servingSize;
//        this.recipeImage = recipeImage;
//    }
//
//    public Recipe(String name, String caption, String category, String type, String duration, int servingSize, String recipeImage) {
//        this.name = name;
//        this.caption = caption;
//        this.category = category;
//        this.type = type;
//        this.duration = duration;
//        this.servingSize = servingSize;
//        this.recipeImage = recipeImage;
//    }

    public Recipe() {}
    public Recipe(String name, String recipeImage) {
        this.name = name;
        this.recipeImage = recipeImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }
    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

}
