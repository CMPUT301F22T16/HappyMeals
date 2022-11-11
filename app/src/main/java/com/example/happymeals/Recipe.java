package com.example.happymeals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a modal class for user's Recipe. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. r_id : A {@link String} id representing document id of the recipe in the database.
 *  2. title : A {@link String} title for the recipe.
 *  3. preparation_time : An {@link Integer} time for number of minutes that it takes to cook the recipe.
 *  4. num_servings : An {@link Integer} representing number of servings for the recipe.
 *  5. category: A {@link String} representing category of the recipe.
 *  6. comments: A {@link List<String>} of comments left by User on their own recipe.
 *  7. ingredients : A {@link List< UserIngredient >} for list of ingredients that this recipe has.
 */
public class Recipe implements Storable, Serializable {
    private String r_id = null; // All ids' are null if not fetched
    private String title;
    private int preparation_time;
    private int num_servings;
    private String category;
    private List<String> comments = new ArrayList<>();
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();
    private List<Long> amounts = new ArrayList<>();

    /**
     * Default constructor for Recipe. Initializes the recipe object with default values.
     */
    public Recipe() {
        this.title = "New Recipe";
        this.preparation_time = 0;
        this.num_servings = 0;
        this.category = "New Category";
        this.comments = new ArrayList<>(Arrays.asList("Nice!"));
        this.recipeIngredients = new ArrayList<>();
    }

    /**
     * Constructor for Recipe with all the member listed here. {@link Recipe}
     * @param title
     * @param preparation_time
     * @param num_servings
     * @param category
     * @param comments
     * @param ingredients
     */
    public Recipe(String title, int preparation_time, int num_servings, String category, List<String> comments, List<RecipeIngredient> ingredients) {
        this.title = title;
        this.preparation_time = preparation_time;
        this.num_servings = num_servings;
        this.category = category;
        this.comments = comments;
        this.recipeIngredients = ingredients;
    }

    /**
     * Get the title of the recipe.
     * @return A {@link String} title of the recipe.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the preparation time for the recipe.
     * @return An {@link Integer} preparation time for the recipe.
     */
    public int getPreparation_time() {
        return preparation_time;
    }

    /**
     * Get the number of servings for the recipe.
     * @return An {@link Integer} number of servings for the recipe.
     */
    public int getNum_servings() {
        return num_servings;
    }

    /**
     * Get the category for the recipe.
     * @return A {@link String} cateory for the recipe.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get the comments for the recipe.
     * @return A {@link List<String>} with all the comments for the recipe.
     */
    public List<String> getComments() {
        return comments;
    }

    /**
     * Get the ingredients for the recipe.
     * @return A {@link List< UserIngredient >} with all the ingredients for the recipe.
     */
    public List<RecipeIngredient> getIngredients() {
        return recipeIngredients;
    }

    /**
     * Set the title for the recipe.
     * @param title The {@link String} title of the recipe to be set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the preparation time for the recipe.
     * @param preparation_time The {@link Integer} preparation time for the recipe.
     */
    public void setPreparation_time(int preparation_time) {
        this.preparation_time = preparation_time;
    }

    /**
     * Set the number of servings for the recipe.
     * @param num_servings The {@link Integer} number of servings for the recipe to be set.
     */
    public void setNum_servings(int num_servings) {
        this.num_servings = num_servings;
    }

    /**
     * Set the category of the recipe.
     * @param category The {@link String} category for the recipe to be set.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Set the ingredients for the recipe object.
     * @param ingredients The {@link List< RecipeIngredient >} list of ingredients to be added to the recipe.
     */
    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.recipeIngredients = ingredients;
    }

    /**
     * Get the document id of the current recipe in the user's database.
     * @return {@link String} document id for the recipe.
     */
    public String get_r_id() {
        return this.r_id;
    }

    /**
     * Sets the returned document id from the firestore.
     * @apiNote Important
     *          Do not use this function outside DBHandler. Doing so may result in the recipe becoming unreachable in the
     *          database.
     * @param id {@link String} id to be set.
     */
    public void setR_id(String id) {this.r_id = id;}

    /**
     * Gets a {@link Storable} {@link HashMap<String, Object>} of data corresponding to the contents of the recipe.
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
    @Override
    public HashMap<String, Object> getStorable() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.title);
        data.put("preparation_time", this.preparation_time);
        data.put("num_servings", this.num_servings);
        data.put("comments", this.comments);
        data.put("category", this.category);

        Map<String, Map<String, Object>> ingredientsMap = new HashMap<>();

        for (RecipeIngredient ingredient : this.recipeIngredients) {
            Map<String, Object> info = new HashMap<>();
            info.put("category", ingredient.getCategory());
            info.put("amount", ingredient.getAmount());
            ingredientsMap.put(ingredient.getDescription(), info);
        }

        data.put("ingredients", ingredientsMap);

        return data;
    }

}
