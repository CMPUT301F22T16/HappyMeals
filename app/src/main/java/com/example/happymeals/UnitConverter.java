package com.example.happymeals;

import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MealPlan;

import java.util.ArrayList;
import java.util.List;

public class UnitConverter {
    static public ArrayList<RecipeIngredient> getShoppingList(MealPlan mealPlan, List<UserIngredient> userIngredients) {
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (List<Meal> meals : mealPlan.getMeals()) {
            for (Meal meal : meals) {
                for (Recipe recipe : meal.getRecipes()) {
                    recipeIngredients.addAll(recipe.getIngredients());
                }
            }
        }
        return recipeIngredients;
    }

    static public RecipeIngredient RtoRIngredientConversion(RecipeIngredient r1, RecipeIngredient r2) {
        return r1;
    }
}

