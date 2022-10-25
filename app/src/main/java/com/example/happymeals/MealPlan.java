package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealPlan implements Storable{
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

    @Override
    public HashMap<String, Object> getStorable() {
        List<Meal> breakfast = this.getBreakfast();
        List<Meal> lunch = this.getLunch();
        List<Meal> dinner = this.getDinner();
        int num_days = this.getNum_days();
        List<String> breakfast_ids = new ArrayList<>();
        List<String> lunch_ids = new ArrayList<>();
        List<String> dinner_ids = new ArrayList<>();

        for (int i = 0; i < num_days; i++) {
            breakfast_ids.add(breakfast.get(i).getM_id());
            lunch_ids.add(lunch.get(i).getM_id());
            dinner_ids.add(dinner.get(i).getM_id());
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("breakfast", breakfast_ids);
        data.put("lunch", lunch_ids);
        data.put("dinner", dinner_ids);
        data.put("num_days", num_days);

        return data;
    }
}
