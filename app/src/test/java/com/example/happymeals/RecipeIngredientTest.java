package com.example.happymeals;

import com.example.happymeals.recipe.RecipeIngredient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RecipeIngredientTest {

    RecipeIngredient ingredient;

    @Before
    public void mockRecipeIngredient() {
        String desc = "Carrot";
        String category = "Vegetable";
        Double amount = 200.0;
        ingredient = new RecipeIngredient(desc, category, amount);
    }

    @Test
    public void testGetDescription() {
        Assert.assertEquals(this.ingredient.getDescription(), "Carrot");
    }

    @Test
    public void testSetDescription() {
        this.ingredient.setDescription("Apple");
        Assert.assertEquals(this.ingredient.getDescription(), "Apple");
    }

    @Test
    public void testGetCategory() {
        Assert.assertEquals(this.ingredient.getCategory(), "Vegetable");
    }

    @Test
    public void testSetCategory() {
        this.ingredient.setCategory("Fruit");
        Assert.assertEquals(this.ingredient.getCategory(), "Fruit");
    }

    @Test
    public void testGetAmount() {
        Assert.assertEquals(this.ingredient.getAmount(),  (Double) 200.0);
    }

    @Test
    public void testSetAmount() {
        this.ingredient.setAmount(300.0);
        Assert.assertEquals(this.ingredient.getAmount(), (Double) 300.0);
    }

    @Test
    public void testGetUnits() {
        Assert.assertEquals(this.ingredient.getUnit(), "qty");
    }

    @Test
    public void testSetUnit() {
        this.ingredient.setUnit("g");
        Assert.assertEquals(this.ingredient.getUnit(), "g");
    }
}
