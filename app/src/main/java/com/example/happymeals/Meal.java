package com.example.happymeals;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    List<Recipe> recipes;
    List<Double> scalings;
    double cost;
    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    public List<Double> getScalings() {
        return this.scalings;
    }

    public double getCost() {
        return this.cost;
    }
}
