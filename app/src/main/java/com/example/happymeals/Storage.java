package com.example.happymeals;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Storage implements Storable {

    private String storeName;
    private String id;
    private List<Ingredient> ingredients = new ArrayList<>();

    public Storage(String storeName, String id, List<Ingredient> ingredients) {
        this.storeName = storeName;
        this.ingredients = ingredients;
    }

    public Storage(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public ArrayList<Ingredient> getIngredients() { return (ArrayList<Ingredient>) this.ingredients; }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public HashMap<String, Object> getStorable() {
        List<String> ingredientsStrs = new ArrayList<>();
        for (int i=0 ;i<ingredients.size(); i++) {
            ingredientsStrs.add(ingredients.get(i).getId());
        }
        HashMap<String, Object> data = new HashMap();
        data.put("type", this.storeName);
        data.put("ingredients", ingredientsStrs);
        return data;
    }
}
