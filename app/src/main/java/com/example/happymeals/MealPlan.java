package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealPlan implements Storable{
    private final Map<String, List<Meal>> meal_plans;
    private final int num_days;
    private String ump_id = null; // ids' null by default if not fetched

    public MealPlan(Map<String, List<Meal>> meal_plans, int num_days) {
        this.meal_plans = meal_plans;
        this.num_days = num_days;
    }

    public Map<String, List<Meal>> getMealPlans(){
        return this.meal_plans;
    }


    public int getNum_days() {
        return num_days;
    }

    public String get_ump_id() {return this.ump_id;};
    public void set_ump_id(String id) {this.ump_id = id;}

    public void setUmp_id(String id) {
        this.ump_id = id;
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

    public Map<String, List<Meal>> getMeal_plans() {
        return meal_plans;
    }
}
