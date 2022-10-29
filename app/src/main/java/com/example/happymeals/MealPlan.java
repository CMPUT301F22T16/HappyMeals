package com.example.happymeals;

import java.util.ArrayList;

public class MealPlan {
    private int meal_plan_duration;
    private ArrayList<Meal> meals;

    public MealPlan(int duration){
        meal_plan_duration = duration;
        meals = new ArrayList<>();
    }


    public int getMeal_plan_duration() {
        return meal_plan_duration;
    }

    public void setMeal_plan_duration(int meal_plan_duration) {
        this.meal_plan_duration = meal_plan_duration;
    }
}

