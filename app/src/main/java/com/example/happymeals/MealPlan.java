package com.example.happymeals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealPlan implements Storable, Serializable {
    private List<Meal> breakfast;
    private List<Meal> lunch;
    private List<Meal> dinner;
    private int num_days;
    private String ump_id = null; // ids' null by default if not fetched

    // TODO: needs a title field
    public MealPlan() {
        this.breakfast = new ArrayList<>();
        this.lunch = new ArrayList<>();
        this.dinner = new ArrayList<>();
        this.num_days = 0;
    }

    public MealPlan(List<Meal> breakfast, List<Meal> lunch, List<Meal> dinner, int num_days) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.num_days = num_days;
    }

    public void setDays(int days) {this.num_days = days;}

    public List<Meal> getBreakfast() {
        return breakfast;
    }

    public List<Meal> getLunch() {
        return lunch;
    }

    public List<Meal> getDinner() {
        return dinner;
    }

    public void setBreakfastWithIndex(Meal meal, int index) {
        if(index >= this.breakfast.size()) {
            this.breakfast.add(meal);
        } else {
            this.breakfast.set(index, meal);
        }
    }

    public void setLunchWithIndex(Meal meal, int index) {
        if(index >= this.lunch.size()){
            this.lunch.add(meal);
        } else {
            this.lunch.set(index, meal);
        }
    }

    public void setDinnerWithIndex(Meal meal, int index) {
        if(index >= this.dinner.size()) {
            this.dinner.add(meal);
        } else {
            this.dinner.set(index, meal);
        }
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
}
