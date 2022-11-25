package com.example.happymeals.meal;

import com.example.happymeals.Recipe;
import com.example.happymeals.Storable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a modal class for user's Meals. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. m_id : A {@link String} id representing document id of the meal in the database.
 *  2. recipes : A {@link List< Recipe >} of recipes associated with the meal.
 *  3. scalings : A {@link List<Double>} of scalings for each recipe that the user can adjust.
 *  4. cost : A {@link Double} total cost for the Meal.
 *  5. title : A {@link String} title for the meal.
 */
public class Meal implements Storable, Serializable {
    private List<Recipe> recipes;
    private Map<String, Double> scalings;
    private double cost;
    private String m_id = null;
    private String title;

    /**
     * A Constructor for Meal using all the member attributes listed {@link Meal}.
     * @param recipes
     * @param scalings
     * @param cost
     * @param title
     */
    public Meal(String title, List<Recipe> recipes, Map<String, Double> scalings, double cost) {
        this.recipes = recipes;
        this.scalings = scalings;
        this.cost = cost;
        this.title = title;
    }

    /**
     * A Default constructor for the Meal object.
     */
    public Meal(){
        this.recipes = new ArrayList<>();
        scalings = new HashMap<>();
        cost = 0;
        this.title = "New Meal";
    }

    /**
     * Get the {@link String} title for the meal.
     * @return {@link String} title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the {@link String} title for the meal
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Removes a recipe from the meal's recipe list specified by the index.
     * @param index An {@link Integer} index for the recipe to be removed.
     */
    public void removeRecipe(int index){
        this.recipes.remove(index);
    }

    /**
     * Add a new recipe to the meal recipes list
     * @param recipe An {@link Recipe} new recipe to be added
     *               to the list
     */
    public void addRecipe(Recipe recipe){
        this.recipes.add(recipe);
    }

    /**
     * Get the recipes associated with the meal object
     * @return {@link List<Recipe>} A list of recipes.
     */
    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    /**
     * Set the recipes assocaited to the meal object.
     * @param recipes {@link List<Recipe>} A list of recipes.
     */
    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
    }

    /**
     * Get the scalings associated with the recipes of the meal object
     * @return {@link List<Recipe>} A list of scalings of type {@link Double}.
     */
    public Map<String, Double> getScalings() {
        return this.scalings;
    }

    /**
     * Set the scalings {@link Map<String, Double>}.
     * @param scalings {@link Map<String, Double>} of scalings to be set.
     */
    public void setScalings(Map<String, Double> scalings) {
        this.scalings = scalings;
    }

    /**
     * Get the scaling corresponding to the recipe in the recipe list.
     * @param recipe {@link Recipe} recipe for which scaling is to be fetched.
     * @return A {@link Double} scaling for the recipe
     * @throws Exception if recipe not found in recipe list
     */
    public Double getScalingForRecipe(Recipe recipe) throws Exception {
        if (this.recipes.contains(recipe) ) {
            return this.scalings.get(recipe.get_r_id());
        }

        else {
            throw new Exception("Recipe not found.");
        }
    }


    /**
     * Sets the scaling for the recipe in the recipe list.
     * @param recipe {@link Recipe} recipe for which the scaling is to be set.
     * @param scaling {@link Double} double value for the scaling.
     * @throws Exception if recipe not found in the list.
     */
    public void setScalingForRecipe(Recipe recipe, Double scaling) throws Exception {
        if (this.recipes.contains(recipe)) {
            this.scalings.put(recipe.get_r_id(), scaling);
        }
        else {
            throw new Exception("Recipe not found.");
        }
    }

    /**
     * Get the total cost of the Meal.
     * @return A {@link Double} cost for the Meal.
     */
    public double getCost() {
        return this.cost;
    }

    /**
     * Get the document id of the meal object in the database.
     * @return {@link String} document id of the Meal.
     */
    public String getM_id() {
        return this.m_id;
    }

    /**
     * Sets the returned document id from the firestore.
     * @apiNote Important
     *          Do not use this function outside DBHandler. Doing so may result in the recipe becoming unreachable in the
     *          database.
     * @param id {@link String} id to be set.
     */
    public void setM_id(String id) {
        this.m_id = id;
    }

    /**
     * Gets a {@link Storable} {@link HashMap<String, Object>} of data corresponding to the contents of the meal.
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
    @Override
    public HashMap<String, Object> getStorable() {
        HashMap<String, Object> data = new HashMap<>();

        // Put the cost
        double cost = this.getCost();
        data.put("cost", cost);

        // Put the recipes
        List<String> recipe_ids = new ArrayList<>();
        for (Recipe recipe : this.recipes) {
            recipe_ids.add(recipe.get_r_id());
        }

        // Put the map for scalings
        data.put("recipe_scalings", this.scalings);


        // Put the title
        data.put("title", this.title);

        return data;
    }

    /**
     * Create an identical copy of this meal
     * @return Meal
     */
    public Meal copy() {
        Meal meal = new Meal(this.title, this.recipes, this.scalings, this.cost);
        meal.setM_id(this.m_id);
        return meal;
    }
}
