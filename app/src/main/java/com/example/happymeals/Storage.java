package com.example.happymeals;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This is a modal class for user's Storages. Implements {@link Storable} in order to store it to the database.
 * Also implements {@link Serializable} in order to pass it between different activities.
 *
 * Members:
 *  1. id : A {@link String} id representing document id of the storage in the database.
 *  2. storeName : A {@link String} representing name of the storage.
 *  3. ingredients : A {@link List< UserIngredient >} of ingredients to be stored in the storage.
 */
public class Storage implements Storable {

    private String storeName;
    private String id;

    private List<UserIngredient> userIngredients = new ArrayList<>();

    /**
     * A constructor for storages with a {@link String} storage name provided.
     * @param storeName
     */
    public Storage(String storeName) {
        this.storeName = storeName;
    }

    /**
     * Get the name of the Storage object.
     * @return {@link String} name of the storage.
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Set the name of the Storage object.
     * @param storeName {@link String} new name of the storage.
     */
    public void setStoreName(String storeName) { this.storeName = storeName; }

    /**
     * Get the document id of the Storage object in the database.
     * @return {@link String} document id of the Storage.
     */
    public String getId() { return id; }

    /**
     * Sets the returned document id from the firestore.
     * @apiNote Important
     *          Do not use this function outside DBHandler. Doing so may result in the recipe becoming unreachable in the
     *          database.
     * @param id {@link String} id to be set.
     */
    public void setId(String id) { this.id = id; }

    /**
     * Get the ingredients present in the Storage object.
     * @return {@link ArrayList< UserIngredient >} of ingredients in the storage.
     */
    public ArrayList<UserIngredient> getIngredients() { return (ArrayList<UserIngredient>) this.userIngredients; }

    /**
     * Add a new ingredient in the storage.
     * @param userIngredient {@link UserIngredient} object to be added.
     */
    public void addIngredient(UserIngredient userIngredient) {
        userIngredients.add(userIngredient);
    }

    /**
     * Gets a {@link Storable} {@link HashMap<String, Object>} of data corresponding to the contents of the meal.
     * @return A {@link Storable} {@link HashMap<String, Object>} of data.
     */
    @Override
    public HashMap<String, Object> getStorable() {
        List<String> ingredientsStrs = new ArrayList<>();
        for (int i = 0; i< userIngredients.size(); i++) {
            ingredientsStrs.add(userIngredients.get(i).getId());
        }
        HashMap<String, Object> data = new HashMap();
        data.put("type", this.storeName);
        data.put("ingredients", ingredientsStrs);
        return data;
    }
}
