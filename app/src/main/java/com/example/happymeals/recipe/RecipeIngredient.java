package com.example.happymeals.recipe;

import com.example.happymeals.Ingredient;

import java.io.Serializable;

/**
 * This class represents the ingredients for the recipes and inherits from {@link Ingredient} some of the attributes.
 * The attributes inherited are the only ones required for the purpose of {@link Recipe}.
 * Members:
 * 1. description - A {@link String} description for the Ingredient.
 * 2. category - A {@link String} category for the ingredient.
 * 3. amount - A {@link Double} amount for the amount of ingredient in the recipe.
 * 4. units - A {@link String} units for the units of the amount for the ingredient.
 *
 * Additionally, this class is {@link Serializable}, which means it can be bundled and passed as serializable.
 */
public class RecipeIngredient extends Ingredient implements Serializable {

    private String description;
    private String category;
    private Double amount;
    private String units;

    /**
     * Default constructor for the {@link RecipeIngredient}.
     * Instantiates a RecipeIngredient with empty description, category and 0.0 amount.
     * Default units are "qty".
     */
    public RecipeIngredient() {
        this.description = "";
        this.category = "";
        this.amount = 0.00;
        this.units = "qty";
    }

    /**
     * Constructor for the {@link RecipeIngredient} class.
     * @param desc A {@link String} description for the ingredient.
     * @param category A {@link String} category for the ingredient.
     * @param amount A {@link Double} amount for the ingredient.
     */
    public RecipeIngredient(String desc, String category, Double amount) {
        this.description = desc;
        this.category = category;
        this.amount = amount;
        this.units = "qty";
    }

    /**
     * This method overrides the {@link Recipe} getDescription() method.
     * @return A {@link String} description for the ingredient.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Setter method for the description.
     * @param description A {@link String} description for the ingredient.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Thi method is overridden getCategory from {@link Ingredient} parent.
     * Getter for the {@link String} category.
     * @return A {@link String} category for the ingredient.
     */
    @Override
    public String getCategory() {
        return category;
    }

    /**
     * Setter for the category member.
     * @param category {@link String} category to set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for the {@link Double} amount of the ingredient.
     * @return {@link Double} amount of the ingredient
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for the {@link Double} amount for the ingredient.
     * @param amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for the {@link String} units of the ingredient.
     * @return A {@link String} units for the ingredient amount.
     */
    public String getUnit() {
        return units;
    }

    /**
     * Setter for the {@link String} units of the ingredient.
     * @param units A {@link String} units for the ingredient amount.
     */
    public void setUnit(String units) {
        this.units = units;
    }
}
