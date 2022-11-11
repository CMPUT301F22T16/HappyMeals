package com.example.happymeals;

import java.io.Serializable;
import java.util.HashMap;

public class RecipeIngredient extends Ingredient implements Serializable {

    private String description;
    private String category;
    private Integer amount;

    public RecipeIngredient() {
        this.description = "";
        this.category = "";
        this.amount = 0;
    }

    public RecipeIngredient(String desc, String category, Integer amount) {
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
