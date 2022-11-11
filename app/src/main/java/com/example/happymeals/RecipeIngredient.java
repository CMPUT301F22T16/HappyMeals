package com.example.happymeals;

import java.io.Serializable;
import java.util.HashMap;

public class RecipeIngredient extends Ingredient implements Serializable {

    private Double amount;

    public RecipeIngredient() {
        this.description = "";
        this.category = "";
        this.amount = 0.0;
    }

    public RecipeIngredient(String desc, String category, Double amount) {
        this.description = desc;
        this.category = category;
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }
}
