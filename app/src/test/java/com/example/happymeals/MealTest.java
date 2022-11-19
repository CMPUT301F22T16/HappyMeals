package com.example.happymeals;

import com.example.happymeals.meal.Meal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealTest {
    Meal meal;
    Recipe recipe;

    @Before
    public void mockMeal(){
        List<Recipe> recipes = new ArrayList<>();
        recipe = new Recipe();
        recipes.add(recipe);
        List<Double> scaling = new ArrayList<>();
        scaling.add(3.14);
        scaling.add(1.11);
        meal = new Meal(recipes,scaling,19.9);
    }

    @Test
    public void testRemoveRecipe(){
        meal.removeRecipe(0);
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
    public void testGetScalings(){
        List<Double> scalings = meal.getScalings();
        Assert.assertTrue(scalings.get(0)==3.14);
        Assert.assertTrue(scalings.get(1)==1.11);
    }

    @Test
    public void testGetCost(){
        Assert.assertTrue(meal.getCost()==19.9);
    }


    @Test
    public void testGetStorable(){
        HashMap<String, Object> data = meal.getStorable();
        Assert.assertEquals(data.get("cost"),19.9);
    }
}
