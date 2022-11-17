package com.example.happymeals;

import com.example.happymeals.meal.Meal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a modal class for user's MealPlans. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. ump_id : A {@link String} id representing document id of the meal plan in the database.
 *  2. breakfast : A {@link List< Meal >} of meals associated with the breakfast in meal plan.
 *  3. lunch : A {@link List<Meal>} of meals associated with the lunch in meal plan.
 *  4. dinner : A {@link List<Meal>} of meals associated with the dinner in meal plan.
 *  5. num_days : An {@link Integer} representing the number of days for which the meal plan is.
 */
public class MealPlan implements Storable, Serializable {
    private List<Meal> breakfast;
    private List<Meal> lunch;
    private List<Meal> dinner;
    private int num_days;
    private String ump_id = null; // ids' null by default if not fetched

    /**
     * Default constructor for mealplan.
     */
    // TODO: needs a title field
    public MealPlan() {
        this.breakfast = new ArrayList<>();
        this.lunch = new ArrayList<>();
        this.dinner = new ArrayList<>();
        this.num_days = 0;
    }

    /**
     * A Constructor for MealPLan using all the member attributes listed {@link MealPlan}.
     * @param breakfast
     * @param lunch
     * @param dinner
     * @param num_days
     */
    public MealPlan(List<Meal> breakfast, List<Meal> lunch, List<Meal> dinner, int num_days) {
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.num_days = num_days;
    }

    /**
     * Set the number of days for which the user chose the meal plan for.
     * @param days An {@link Integer} number of days for which the meal plan is.
     */
    public void setDays(int days) {this.num_days = days;}

    /**
     * Get the {@link List<Meal>} of breakfast items for each day in meal plan.
     * @return A {@link List<Meal>} of meals for breakfast.
     */
    public List<Meal> getBreakfast() {
        return breakfast;
    }

    /**
     * Get the {@link List<Meal>} of lunch items for each day in meal plan.
     * @return A {@link List<Meal>} of meals for lunch.
     */
    public List<Meal> getLunch() {
        return lunch;
    }

    /**
     * Get the {@link List<Meal>} of dinner items for each day in meal plan.
     * @return A {@link List<Meal>} of meals for dinner.
     */
    public List<Meal> getDinner() {
        return dinner;
    }

    /**
     * Set the Breakfast item at the given index in the Meal Plan.
     * @param meal A {@link Meal} object to set at index;
     * @param index An {@link Integer} index specifying position.
     */
    public void setBreakfastWithIndex(Meal meal, int index) {
        if(index >= this.breakfast.size()) {
            this.breakfast.add(meal);
        } else {
            this.breakfast.set(index, meal);
        }
    }

    /**
     * Set the lunch item at the given index in the Meal Plan.
     * @param meal A {@link Meal} object to set at index;
     * @param index An {@link Integer} index specifying position.
     */
    public void setLunchWithIndex(Meal meal, int index) {
        if(index >= this.lunch.size()){
            this.lunch.add(meal);
        } else {
            this.lunch.set(index, meal);
        }
    }

    /**
     * Set the dinner item at the given index in the Meal Plan.
     * @param meal A {@link Meal} object to set at index;
     * @param index An {@link Integer} index specifying position.
     */
    public void setDinnerWithIndex(Meal meal, int index) {
        if(index >= this.dinner.size()) {
            this.dinner.add(meal);
        } else {
            this.dinner.set(index, meal);
        }
    }

    /**
     * Get the number of days for which the meal plan is.
     * @return An {@link Integer} representing the number of days.
     */
    public int getNum_days() {
        return num_days;
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
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
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
