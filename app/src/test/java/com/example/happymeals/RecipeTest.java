package com.example.happymeals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeTest {

    Recipe recipe;

    @Before
    public void mockRecipe() {
        recipe = new Recipe();
    }

    @Test
    public void testGetTitle() {
        Assert.assertEquals(recipe.getTitle(), "New Recipe");
    }

    @Test
    public void testSetTitle() {
        recipe.setTitle("Broccoli");
        Assert.assertEquals(recipe.getTitle(), "Broccoli");
    }

    @Test
    public void testGetPreparationTime() {
        Assert.assertEquals(recipe.getPreparation_time(), 0);
    }

    @Test
    public void testSetPreparationTime() {
        recipe.setPreparation_time(5);
        Assert.assertEquals(recipe.getPreparation_time(), 5);
    }

    @Test
    public void testGetNumServings() {
        Assert.assertEquals(recipe.getNum_servings(), 0);
    }

    @Test
    public void testSetNumServings() {
        recipe.setNum_servings(5);
        Assert.assertEquals(recipe.getNum_servings(), 5);
    }

    @Test
    public void testGetCategory() {
        Assert.assertEquals(recipe.getCategory(), "New Category");
    }

    @Test
    public void testSetCategory() {
        recipe.setCategory("Vegetable");
        Assert.assertEquals(recipe.getCategory(), "Vegetable");
    }

    @Test
    public void testGetComments() {
        for (String comment : recipe.getComments()) {
            Assert.assertEquals(comment, "Nice!");
        }
    }

    @Test
    public void testGetIngredients() {
        Assert.assertEquals(recipe.getIngredients().size(), 0);
    }

    @Test
    public void testGetStorable() {
        HashMap<String, Object> data = recipe.getStorable();
        Assert.assertEquals((String) data.get("title"), "New Recipe");
        Assert.assertEquals(data.get("preparation_time"), 0);
        Assert.assertEquals(data.get("num_servings"), 0);
    }

}
