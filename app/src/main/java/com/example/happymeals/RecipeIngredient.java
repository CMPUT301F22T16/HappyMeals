package com.example.happymeals;

import java.io.Serializable;
import java.util.HashMap;

public class RecipeIngredient extends Ingredient implements Serializable {

    private String description;
    private String category;
    private Double amount;
    private String units;

    public RecipeIngredient() {
        this.description = "";
        this.category = "";
        this.amount = 0.00;
        this.units = "qty";
    }

    public RecipeIngredient(String desc, String category, Double amount) {
        this.description = desc;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }
}
