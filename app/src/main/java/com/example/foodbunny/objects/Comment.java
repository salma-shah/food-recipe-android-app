package com.example.foodbunny.objects;

public class Comment {
    private int id, userId, recipeId;
    private String comment, datePosted;
    private User user;
    public Comment() {
    }

//    public Comment(int id, int userId, int recipeId, String comment, String datePosted) {
//        this.id = id;
//        this.userId = userId;
//        this.recipeId = recipeId;
//        this.comment = comment;
//        this.datePosted = datePosted;
//    }

    public int getId()
    {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(String datePosted) {
        this.datePosted = datePosted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
