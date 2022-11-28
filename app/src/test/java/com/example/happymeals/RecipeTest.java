package com.example.happymeals;

import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeIngredient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void testSetIngredients() {
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(recipeIngredient);
        this.recipe.setIngredients(ingredients);

        Assert.assertEquals(recipe.getIngredients().size(), 1);
    }


    @Test
    public void testGetDownloadUri() {
        Assert.assertEquals(this.recipe.getDownloadUri(), "android.resource://com.example.happymeals/drawable/recipe_default");
    }

    @Test
    public void testSetDownloadUri() {
        this.recipe.setDownloadUri("filepath");
        Assert.assertEquals(this.recipe.getDownloadUri(), "filepath");
    }


    @Test
    public void testGetStorable() {

        // Adding ingredient
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setCategory("Fruit");
        ingredient.setDescription("Apple");
        ingredient.setAmount(1.0);
        ingredient.setUnit("kg");
        List<RecipeIngredient> ingredients = new ArrayList<>();
        ingredients.add(ingredient);
        this.recipe.setIngredients(ingredients);


        HashMap<String, Object> data = recipe.getStorable();
        Assert.assertEquals((String) data.get("title"), "New Recipe");
        Assert.assertEquals(data.get("preparation_time"), 0);
        Assert.assertEquals(data.get("num_servings"), 0);
        Assert.assertEquals(data.get("category"), "New Category");
        List<String> comments = (List<String>) data.get("comments");
        Assert.assertTrue(comments.contains("Nice!"));

        Map<String, Map<String, Object>> ingredientsMap = (Map<String, Map<String, Object>>) data.get("ingredients");

        Set<String> recipeDescs = ingredientsMap.keySet();


        Assert.assertTrue(recipeDescs.contains("Apple"));

        Map<String, Object> ingredientInfo = ingredientsMap.get("Apple");

        Assert.assertEquals(ingredientInfo.get("amount"), 1.0);
        Assert.assertEquals(ingredientInfo.get("category"), "Fruit");
        Assert.assertEquals(ingredientInfo.get("units"), "kg");

    }

}
