package com.example.happymeals;

import java.util.ArrayList;
import java.util.List;

public class Meal {
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
}
