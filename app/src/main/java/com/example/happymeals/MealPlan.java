package com.example.happymeals;

import static java.sql.DriverManager.println;

import android.widget.ArrayAdapter;

import com.example.happymeals.meal.Meal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a modal class for user's MealPlans. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. ump_id : A {@link String} id representing document id of the meal plan in the database.
 *  2. title : A {@link String} title for the meal plan.
 *  3. mealplan : A {@link List<List<Meal>>} list of list each representing 1 day of the MealPlan.
 *  4. num_days : An {@link Integer} representing the number of days for which the meal plan is.
 */
public class MealPlan implements Storable, Serializable {
    private List<List<Meal>> mealplan;
    private int num_days;
    private String title;
    private String ump_id = null; // ids' null by default if not fetched

    /**
     * Default constructor for mealplan.
     */
    public MealPlan() {
        this.title = "New MealPlan";
        this.num_days = 0;
        this.mealplan = new ArrayList<>();
    }


    /**
     * A Constructor for MealPLan using all the member attributes listed {@link MealPlan}.
     * @param title
     * @param mealplan
     * @param num_days
     */
    public MealPlan(String title, List<List<Meal>> mealplan, int num_days) {
        this.title = title;
        this.mealplan = mealplan;
        this.num_days = num_days;
    }

    /**
     * Set the number of days for which the user chose the meal plan for.
     */
    public void setDays() {this.num_days = this.getNum_days();}

    /**
     * Get the {@link List<List<Meal>>} of meals for each day in meal plan.
     * @return A {@link List<List<Meal>>} of meals for mealplan.
     */
    public List<List<Meal>> getMeals() {
        return this.mealplan;
    }

    /**
     * Set the Breakfast item at the given index in the Meal Plan.
     * @param meal A {@link Meal} object to set at index;
     * @param index An {@link Integer} index specifying position.
     */
    public void setMealWithDayAndIndex(Meal meal, int day, int index) {
        if (mealplan.size()<=day) {
            List<Meal> list = new ArrayList<Meal>();
            list.add(meal);
            mealplan.add(list);
        } else if (mealplan.get(day).size()<=index) {
            mealplan.get(day).add(meal);
        } else {
            this.mealplan.get(day).set(index, meal);
        }
    }

    public void clearMealsWithDay(int day) {
        mealplan.get(day).clear();
    }

    public void loadMealsWithDay(int day, ArrayList<Meal> meals) {
        mealplan.get(day).addAll(meals);
    }

    /**
     * Get the {@link String} title for the mealplan.
     * @return {@link String} title for the mealplan.
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the number of days for which the meal plan is.
     * @return An {@link Integer} representing the number of days.
     */
    public int getNum_days() {
        return mealplan.size();
    }

    /**
     * Get the document id of the MealPlan object in the database.
     * @return {@link String} document id of the MealPlan.
     */
    public String get_ump_id() {return this.ump_id;};

    /**
     * Sets the returned document id from the firestore.
     * @apiNote Important
     *          Do not use this function outside DBHandler. Doing so may result in the recipe becoming unreachable in the
     *          database.
     * @param id {@link String} id to be set.
     */
    public void setUmp_id(String id) {
        this.ump_id = id;
    }

    /**
     * Gets a {@link Storable} {@link HashMap<String, Object>} of data corresponding to the contents of the meal.
     * Here conversion of {@link List<List<Meal>>} to {@link List<Map<String, String>>} is required because firebase doesn't accept nested arrays.
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
    @Override
    public HashMap<String, Object> getStorable() {

        List<Map<String, String>> plans = new ArrayList<>();

        for (List<Meal> dayList : this.mealplan) {

            Map<String, String> dayMap = new HashMap<>();
            int count = 0;
            for (Meal meal : dayList) {
                dayMap.put(String.valueOf(count), meal.getM_id());
                count ++;
            }

            plans.add(dayMap);
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("num_days", this.getNum_days());
        data.put("title", this.getTitle());
        data.put("plans", plans);

        return data;
    }
}
