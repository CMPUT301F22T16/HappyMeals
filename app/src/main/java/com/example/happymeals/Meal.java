package com.example.happymeals;

import java.util.ArrayList;

public class Meal {
    private ArrayList<Recipe> recipes;
    private double total_cost;

    public Meal(){
        total_cost = 0;
        recipes = new ArrayList<>();
    }

    public void addRecipe(Recipe recipe){
        // add a recipe to list
        // i don't think we need to check if contains here
        // update total cost
    }

    public void removeRecipe(Recipe recipe){
        // remove
        // update total cost
    }

    public double getTotal_cost(){
        return total_cost;
    }



}


