package com.example.happymeals.ingredient;

public abstract class Ingredient {

    protected String description;
    protected String category;

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public abstract Double getAmount();

    public abstract void setAmount(Double amount);

    public abstract String getUnit();

    public abstract void setUnit(String units);
}
