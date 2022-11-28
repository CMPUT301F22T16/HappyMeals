package com.example.happymeals.ingredient;


import com.example.happymeals.Storable;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


import java.util.HashMap;

/**
 * This is a modal class for user's Ingredients. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. id : A {@link String} id representing document id of the meal in the database.
 *  2. category :  {@link String} category for the Ingredient.
 *  3. amount : {@link Integer} amount of the ingredient.
 *  4. cost : {@link Double} cost of the ingredient.
 *  5. date : {@link Date} Best before date after which ingredient expires.
 *  6. description : {@link String} Brief description of the ingredient
 *  7. loc : {@link String} Location name where the ingredient is to be stored
 */
public class UserIngredient extends Ingredient implements Storable, Serializable {

    private String category;
    private String description;
    private Double amount;
    private Double cost;
    private Date date;
    private String loc;
    private String id;
    private String unit;
    private Boolean incomplete;

    /**
     * A constructor with all the member attributes here {@link UserIngredient}.
     * @param category
     * @param description
     * @param amount
     * @param cost
     * @param date
     * @param locRef
     */
    public UserIngredient(String category, String description, Double amount, Double cost, Date date, String locRef, String unit) {

        this.category = category;
        this.description = description;
        this.amount = amount;
        this.cost = cost;
        this.date = date;
        this.loc = locRef;
        this.unit = unit;
        this.incomplete = false;
        this.id = null;
    }

    /**
     * A constructor for generic Ingredient that is not an entity yet. Abstract ingredients are required for recipes.
     * @param amount : {@link Integer} amount of ingredient
     * @param description : {@link String} description of the Ingredient
     */
    public UserIngredient(Double amount, String description) {
        this.amount = amount;
        this.description = description;
    }

    /**
     * Get the category of the ingredient.
     * @return
     */
    public String getCategory() { return  category; }

    /**
     * Get the description of the ingredient.
     * @return
     */
    public String getDescription() { return description; }

    /**
     * Get the amount of the ingredient.
     * @return
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Get the cost of the ingredient.
     * @return
     */
    public Double getCost() {
        return cost;
    }

    /**
     * Get the date of the ingredient.
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Get the Location of the ingredient.
     * @return
     */
    public String getLoc() {
        return loc;
    }

    /**
     * Get the unit of the ingredient.
     * @return
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Get the document id of the ingredient.
     * @return
     */
    public String getId() { return id; }

    /**
     * Get the year of date of ingredient.
     * @return
     */
    public int getYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * Get the month of date of ingredient.
     * @return
     */
    public int getMonth(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * Get the day of date of ingredient.
     * @return
     */
    public int getDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Set the category of the ingredient.
     * @param category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Set the description of the ingredient.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the amount of the ingredient.
     * @param amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Set the cost of the ingredient.
     * @param cost
     */
    public void setCost(Double cost) {
        this.cost = cost;
    }

    /**
     * Set the date of the ingredient.
     * @param year
     * @param month
     * @param day
     */
    public void setDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        this.date = cal.getTime();
    }

    /**
     * Set the location of the ingredient.
     * @param loc
     */
    public void setLoc(String loc) {
        this.loc = loc;
    }

    /**
     * Set the unit of the ingredient.
     * @param unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Mark this ingredient as incomplete/complete.
     */
    public void setIncomplete(Boolean status) {
        this.incomplete = status;
    }

    /**
     * Set the id of ingredient.
     * @param id
     */
    public void setId(String id) { this.id = id; }


    /**
     * Gets a {@link Storable} {@link HashMap<String, Object>} of data corresponding to the contents of the meal.
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
    @Override
    public HashMap<String, Object> getStorable() {
        HashMap<String, Object> data = new HashMap();
        data.put("category", this.category);
        data.put("description", this.description);
        data.put("amount", this.amount);
        data.put("cost", this.cost);
        data.put("date", this.date);
        data.put("location", this.loc);
        data.put("unit", this.unit);
        data.put("incomplete", this.incomplete);
        return data;
    }
}
