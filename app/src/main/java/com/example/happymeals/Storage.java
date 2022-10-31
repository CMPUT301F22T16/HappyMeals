package com.example.happymeals;

import java.util.HashMap;
import java.util.List;

public class Storage implements Storable {

    private String storeName;
    private String id;
    private List<String> ingredients;

    public Storage(String storeName, String id, List<String> ingredients) {
        this.storeName = storeName;
        this.ingredients = ingredients;
    }

    public Storage(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient.getId());
    }

    @Override
    public HashMap<String, Object> getStorable() {
        HashMap<String, Object> data = new HashMap();
        data.put("type", this.storeName);
        data.put("ingredients", this.ingredients);
        return data;
    }
}
