package com.example.happymeals.meal;

import com.example.happymeals.Storable;
import com.example.happymeals.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a modal class for user's Meals. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. m_id : A {@link String} id representing document id of the meal in the database.
 *  2. recipes : A {@link List<Recipe>} of recipes associated with the meal.
 *  3. scalings : A {@link List<Double>} of scalings for each recipe that the user can adjust.
 *  4. cost : A {@link Double} total cost for the Meal.
 */
public class Meal implements Storable, Serializable {
    private List<Recipe> recipes;
    private final List<Double> scalings;
    private final double cost;
    private String m_id = null;

    /**
     * A Constructor for Meal using all the member attributes listed {@link Meal}.
     * @param recipes
     * @param scalings
     * @param cost
     */
    // TODO: needs a title field
    public Meal(List<Recipe> recipes, List<Double> scalings, double cost) {
        this.recipes = recipes;
        this.scalings = scalings;
        this.cost = cost;
    }

    /**
     * A Default constructor for the Meal object.
     */
    public Meal(){
        this.recipes = new ArrayList<>();
        scalings = new ArrayList<>();
        cost = 0;
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
    public List<Double> getScalings() {
        return this.scalings;
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
        List<Recipe> recipes = this.getRecipes();
        List<Double> scalings = this.getScalings();
        double cost = this.getCost();
        data.put("cost", cost);
        data.put("scalings", scalings);
        List<String> recipe_ids = new ArrayList<>();
        for (Recipe recipe: recipes) {
            recipe_ids.add(recipe.get_r_id());
        }
        data.put("recipes", recipe_ids);

        return data;
    }
}
