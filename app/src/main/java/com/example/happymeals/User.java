package com.example.happymeals;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Represents a User object. User class maintains a username and also has a database connection established that it can use to add/modify/delete
 * content of following collections in Firestore
 * 1. user_ingredients: {@link Ingredient} is model class for this.
 * 2. users: {@link User} is model class for this.
 * 3. user_storages: {@link Storage} is the model class for this.
 * 4. user_recipes: {@link Recipe} is the model class for this.
 * 5. user_meals: {@link Meal} is the model class for this.
 * 6. user_mealplans: {@link MealPlan} is the model class for this.
 *
 */
public class User{
    /**
     * Members
     *  username: A {@link String} username that represents the document id of the user document in database.
     *  conn: A {@link FirebaseFirestore} database connection to add/modify/delete/query data upon request.
     */
    private String username;
    private FirebaseFirestore conn;
    private List<Storage> storages = new ArrayList<>();
    private List<Ingredient> ingredients = new ArrayList<>();

    public User() {
        username = "Guest"; // This is temporary
        conn = FirebaseFirestore.getInstance();
        storagesListenAndUpdate();
    }

    public User(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
    }

    public String getUsername() {
        return this.username;
    }

    public FirebaseFirestore getConn() {
        return this.conn;
    }

    public void newUser(Context context, String username) {
        HashMap<String, String> data = new HashMap<>();
        data.put("pass", "");
        CollectionReference ref = conn.collection("users");
        ref //Did not use the store method because adding a new user requires a different set of methods.
                .document(username)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Stor", "Data has been added successfully!");
                        Toast.makeText(context,  "New user added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Stor", "Data could not be added!" + e.toString());
                        Toast.makeText(context, "New user could not be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String store(CollectionReference ref, HashMap data, String collType, Context context) {
        String id;
        DocumentReference doc = ref.document();
        doc
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("store", "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("store", "Data could not be added!" + e.toString());
                        Toast.makeText(context, "Error: " + collType + "could not be added", Toast.LENGTH_SHORT).show();
                    }
                });

        return doc.getId();
    }

    private void delete(CollectionReference ref, String id, String collType, Context context) {
        ref
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("delete", "Data has been deleted successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("delete", "Data could not be deleted!" + e.toString());
                        Toast.makeText(context, "Error: " + collType + " not be deleted", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void update(CollectionReference ref, String id, HashMap<String, Object> data, String collType, Context context) {
        ref
                .document(id)
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("update", "Data has been updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("update", "Data could not be updated!" + e.toString());
                        Toast.makeText(context, "Error: " + collType + " not be updated", Toast.LENGTH_SHORT);
                    }
                });
    }

    //-------------------------------------------------Storage Methods-------------------------------------------------//
    /**
     * Keeps checking for changes in a user's query for storages and updates their storages if change is found.
     */
    private void storagesListenAndUpdate() {
        Query query = conn.collection("storages").whereEqualTo("user", getUsername());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("uStor", "An error has occured while trying to update local storages");
                }
                else {
                    storages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        List<String> ingIds = (List<String>) doc.get("ingredients");
                        for (int i=0; i<ingIds.size(); i++) {
                            for (int j=0; j<ingredients.size(); i++) {
                                if (ingIds.get(i) == ingredients.get(j).getId()) {
                                    ingredients.get(j).setLoc(doc.getString("type"));
                                }
                            }
                        }
                        Storage storage = new Storage(doc.getString("type"), doc.getId(), ingredients);
                        storages.add(storage);
                    }
                    Log.d("uStor", "local storages updated successfully!");
                }
            }
        });
    }

    /**
     * Gets the storages associated with the current user.
     * @return List of Objects of class Storage.
     */
    public List<Storage> getStorages() {
        return this.storages;
    }

    /**
     * Stores a {@link Storage} object in the database, and attaches the database ID of the entry to the object.
     * @param storage : Object of type {@link Storage} that will be stored in the database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void newStorage(Storage storage, Context context) {
        HashMap<String, Object> data = storage.getStorable();
        data.put("user", getUsername());
        CollectionReference ref = conn.collection("storages");
        String id = store(ref, data, "Storage", context);
        storage.setId(id);
    }

    /**
     * Deletes the entry of a {@link Storage} object from the database.
     * @param storage : Object of type {@link Storage} that will be removed from the database
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void deleteStorage(Storage storage, Context context) {
        CollectionReference ref = conn.collection("storages");
        delete(ref, storage.getId(), "Storage", context);
    }

    /**
     * Updates the entry of {@link Storage} object in the database.
     * @param storage : Object of type {@link Storage} to be updated in the database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void updateStorage(Storage storage, Context context) {
        CollectionReference ref = conn.collection("storages");
        update(ref, storage.getId(), storage.getStorable(), "Storage", context);
    }

    //-------------------------------------------------Ingredient Methods-------------------------------------------------//

    /**
     * Stores a {@link Ingredient} object in the database, and attaches the database ID of the entry to the object.
     * @param ingredient : Object of type {@link Storage} that will be added to the database
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void newIngredient(Ingredient ingredient, Context context) {
        HashMap<String, Object> data  = ingredient.getStorable();
        data.put("user", getUsername());
        CollectionReference ref = conn.collection("user_ingredients");
        String id = store(ref, data, "Ingredient", context);
        ingredient.setId(id);
    }

    /**
     * Deletes the entry of a {@link Ingredient} object from the database.
     * @param ingredient : Object of type {@link Ingredient} that will be removed from the database
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void deleteIngredient(Ingredient ingredient, Context context) {
        CollectionReference ref = conn.collection("user_ingredients");
        delete(ref, ingredient.getId(), "Ingredient", context);
    }

    /**
     * Updates the entry of {@link Ingredient} object in the database.
     * @param ingredient : Object of type {@link Ingredient} to be updated in the database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user on failure.
     */
    public void updateIngredient(Ingredient ingredient, Context context) {
        CollectionReference ref = conn.collection("user_ingredients");
        update(ref, ingredient.getId(), ingredient.getStorable(), "Ingredient", context);
    }

    /**
     * Keeps checking for changes in a user's query for user_ingredients and updates their ingredients if change is found.
     */
    private void ingredientListenAndUpdate() {
        Query query = conn.collection("user_ingredients").whereEqualTo("user", getUsername());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("uIng", "An error has occured while trying to update the local ingredients");
                }
                else {
                    ingredients.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String category = doc.getString("category");
                        String description = doc.getString("description");
                        Integer amount = 1; //COME BACK TO THIS LATER
                        Integer cost = 1;
                        Date date = new Date();
                        Ingredient ingredient = new Ingredient(category, description, amount, cost, date);
                        ingredients.add(ingredient);
                    }
                    storagesListenAndUpdate();
                    Log.d("uIng", "Local ingredients updated successfully!");
                }
            }
        });
    }

    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    //-------------------------------------------------Meal Methods-------------------------------------------------//


    /**
     * Used to add Meals to user's database
     * @param meal : A meal of type {@link Meal} containing information to be added to the database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user about success.
     */
    public void addMeal(Meal meal, Context context) {
        HashMap<String, Object> data = meal.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_meals = this.getConn().collection("user_meals");
        String id = store(user_meals, data,"Meal", context);
        meal.setM_id(id);
    }

    public void updateMeal(Meal new_meal, Context context) {
        CollectionReference ref = conn.collection("user_meals");
        update(ref, new_meal.getM_id(), new_meal.getStorable(), "Meal", context);
    }

    public void removeMeal(Meal meal, Context context) {
        CollectionReference ref = conn.collection("user_mealplans");
        delete(ref, meal.getM_id(), "Meal", context);
    }

    /**
     * Used to add Meal plans to user's database.
     * @param mealplan : A meal plan of type {@link MealPlan} containing information for breakfast, lunch, dinner meals to be added to database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user about success.
     */
    public void addMealPlan(MealPlan mealplan, Context context) {
        // TODO Do we need to check a unique constraint for this?
        HashMap<String, Object> data = mealplan.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_mealplans = this.getConn().collection("user_mealplans");
        String id = store(user_mealplans, data,"Meal Plan", context);
        mealplan.setUmp_id(id);
    }

    public void updateMealPlan(MealPlan mealPlan, Context context) {

        CollectionReference ref = conn.collection("user_mealplans");
        update(ref, mealPlan.get_ump_id(), mealPlan.getStorable(), "user_mealplans", context);        }


    public void removeMealPlan(MealPlan mealPlan, Context context) {
        CollectionReference ref = conn.collection("user_mealplans");
        delete(ref, mealPlan.get_ump_id(), "Meal Plan", context);
    }

    public void addRecipe(Recipe recipe, Context context) {
        HashMap<String, Object> data = recipe.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_recipes = this.getConn().collection("user_recipes");
        store(user_recipes, data,"Recipes", context);
    }

    public void modifyRecipe(Recipe new_recipe, Context context) {
        HashMap<String, Object> data = new_recipe.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_recipes = this.getConn().collection("user_recipes");

        try {
            DocumentReference doc = user_recipes.document(new_recipe.get_r_id());
            doc.update(data);
            Toast.makeText(context, "Recipe updated successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void removeRecipe(Recipe recipe, Context context) {

    }

}
