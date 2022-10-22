package com.example.happymeals;

import java.util.List;

public class MealPlan {
    List<Meal> breakfast;
    List<Meal> lunch;
    List<Meal> dinner;
    int num_days;
    String ump_id;

    public List<Meal> getBreakfast() {
        return breakfast;
    }

    public List<Meal> getLunch() {
        return lunch;
    }

    public List<Meal> getDinner() {
        return dinner;
    }

    public int getNum_days() {
        return num_days;
    }
}
