package com.example.happymeals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Recipe implements Storable, Serializable {
    private String r_id = null; // All ids' are null if not fetched
    private String title;
    private int preparation_time;
    private int num_servings;
    private String category;
    private List<String> comments;
    private List<Ingredient> ingredients;

    public Recipe() {
        this.title = "New Recipe";
        this.preparation_time = 0;
        this.num_servings = 0;
        this.category = "New Category";
        this.comments = new ArrayList<>(Arrays.asList("Nice!", "Awesome!"));
        this.ingredients = new ArrayList<>();
    }

    public Recipe(String title, int preparation_time, int num_servings, String category, List<String> comments, List<Ingredient> ingredients) {
        this.title = title;
        this.preparation_time = preparation_time;
        this.num_servings = num_servings;
        this.category = category;
        this.comments = comments;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public int getPreparation_time() {
        return preparation_time;
    }

    public int getNum_servings() {
        return num_servings;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getComments() {
        return comments;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public String get_r_id() {
        return this.r_id;
    }

    public void setR_id(String id) {this.r_id = id;}

    @Override
    public HashMap<String, Object> getStorable() {

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", this.title);
        data.put("preparation_time", this.preparation_time);
        data.put("num_servings", this.num_servings);
        data.put("comments", this.comments);
        data.put("category", this.category);

        List<String> ingredient_ids = new ArrayList<>();
        for (Ingredient ingredient : this.ingredients) {
            ingredient_ids.add("/user_ingredients/" + ingredient.getId());
        }

        data.put("ingredients", ingredient_ids);

        return data;
    }

}
