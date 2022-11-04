package com.example.happymeals;

import com.example.happymeals.Storable;
import com.example.happymeals.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Meal implements Storable, Serializable {
    private final List<Recipe> recipes;
    private final List<Double> scalings;
    private final double cost;
    private String m_id = null;

    public Meal(List<Recipe> recipes, List<Double> scalings, double cost) {
        this.recipes = recipes;
        this.scalings = scalings;
        this.cost = cost;
    }

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

    public void setM_id(String id) {
        this.m_id = id;
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
            recipe_ids.add("/user_recipes/" + recipe.get_r_id());
        }

        data.put("recipes", recipe_ids);

        return data;
    }
}
