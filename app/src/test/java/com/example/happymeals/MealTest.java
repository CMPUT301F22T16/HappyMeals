package com.example.happymeals;

import com.example.happymeals.meal.Meal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kotlin.jvm.Throws;

public class MealTest {
    Meal meal;
    Recipe recipe;

    @Before
    public void mockMeal(){
        List<Recipe> recipes = new ArrayList<>();
        recipe = new Recipe();
        recipe.setR_id("test111id");
        recipes.add(recipe);
        Map<String,Double> scaling = new HashMap<>();
        scaling.put(recipe.get_r_id(), 1.11);
        meal = new Meal("Breakfast",recipes,scaling,19.9);
    }

    @Test
    public void testGetTitle() {
        String title = meal.getTitle();
        Assert.assertEquals(title, "Breakfast");
    }

    @Test
    public void testSetTitle() {
        String new_title = "Lunch";
        this.meal.setTitle(new_title);
        Assert.assertEquals(this.meal.getTitle(), "Lunch");
    }


    @Test
    public void testRemoveRecipe(){
        meal.removeRecipe(recipe);
        Assert.assertEquals(meal.getRecipes().size(),0);
    }

    @Test
    public void testSetRecipe(){
        List<Recipe> r = new ArrayList<>();
        r.add(new Recipe());
        r.add(new Recipe());
        meal.setRecipes(r);
        Assert.assertEquals(meal.getRecipes().size(),2);
    }

    @Test
    public void testaddRecipe(){
        Assert.assertEquals(1,meal.getRecipes().size());
        meal.addRecipe(recipe);
        Assert.assertEquals(2,meal.getRecipes().size());

    }

    @Test
    public void testGetRecipe(){
        List<Recipe> r = meal.getRecipes();
        Assert.assertEquals(r.get(0),recipe);
    }

    @Test
    public void testSetScalings(){
        Map<String,Double> scalings = new HashMap<>();
        scalings.put("test222id",2.22);
        scalings.put("test333id",3.33);
        meal.setScalings(scalings);
        Assert.assertTrue(scalings.get("test222id")==2.22);
        Assert.assertTrue(scalings.get("test333id")==3.33);
    }

    @Test
    public void testGetScalings(){
        Map<String,Double> scalings = meal.getScalings();
        Assert.assertTrue(scalings.get("test111id")==1.11);
    }

    @Test
    public void testGetScalingForRecipe() throws Exception {
        Double scaling = meal.getScalingForRecipe(recipe);
        Assert.assertTrue(scaling == 1.11);
    }

    @Test
    public void testGetScalingForRecipeException() throws Exception{
        Recipe recipe2 = new Recipe();
        recipe2.setR_id("test000id");
        Exception e = Assert.assertThrows(Exception.class,()->meal.getScalingForRecipe(recipe2));
        Assert.assertTrue(e.getMessage().contains("Recipe not found"));
    }

    @Test
    public void testRemoveScalingForRecipe() {
        meal.removeScalingForRecipe(recipe);
        Assert.assertTrue(meal.getScalings().size()==0);
    }




    @Test
    public void testGetCost(){
        Assert.assertTrue(meal.getCost()==19.9);
    }


    @Test
    public void testGetStorable(){
        HashMap<String, Object> data = meal.getStorable();
        Assert.assertEquals(data.get("cost"),19.9);
        String title = (String) data.get("title");
        Map<String, Double> recipe_scalings = (Map<String, Double>) data.get("recipe_scalings");
        Set<String> recipes = recipe_scalings.keySet();
        Assert.assertTrue(recipes.contains("test111id"));
        Collection<Double> scalings = recipe_scalings.values();
        Assert.assertTrue(scalings.contains(1.11));
    }
}
