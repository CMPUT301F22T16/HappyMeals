package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Meal implements Storable {
    List<Recipe> recipes;
    List<Double> scalings;
    double cost;
    String m_id;

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    public List<Double> getScalings() {
        return this.scalings;
    }

    public double getCost() {
        return this.cost;
    }

    public String getM_id() {
        return this.m_id;
    }

    @Override
    public HashMap<String, Object> getStorable() {
        HashMap<String, Object> data = new HashMap<>();
        List<Recipe> recipes = this.getRecipes();
        List<Double> scalings = this.getScalings();
        double cost = this.getCost();
        data.put("cost", cost);
        data.put("scalings", scalings);
        List<String> recipe_ids = new ArrayList<>();
        for (Recipe recipe: recipes) {
            recipe_ids.add(recipe.get_r_id());
        }

        data.put("recipes", recipe_ids);

        return data;
    }
}
