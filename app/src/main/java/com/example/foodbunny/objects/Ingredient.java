package com.example.foodbunny.objects;

public class Ingredient {
    private String unit;
    private double qty;
    private String name;
    public Ingredient() {}

    public Ingredient(String unit, double qty, String name) {
        this.unit = unit;
        this.qty = qty;
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
