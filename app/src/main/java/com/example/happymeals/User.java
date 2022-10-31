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
import java.util.Map;


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

    private void store(CollectionReference ref, String collType, HashMap data, Context context) {
        String id;
        ref
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("store", "Data has been added successfully!");
                        //id = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("store", "Data could not be added!" + e.toString());
                        Toast.makeText(context, collType + " not be added", Toast.LENGTH_SHORT).show();
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
                    Log.d("uStor", "An error has occured while trying to update storages");
                }
                else {
                    storages.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        List<String> ingredients = (List<String>) doc.get("ingredients");
                        Storage storage = new Storage(doc.getString("type"), doc.getId(), ingredients);
                        storages.add(storage);
                    }
                    Log.d("uStor", "Storages updated successfully!");
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

    public void newStorage(Context context, Storage storage) {
        HashMap<String, Object> data = storage.getStorable();
        data.put("user", getUsername());
        CollectionReference ref = conn.collection("storages");
        store(ref, "Storage", data, context);
    }

    public void deleteStorage(Storage storage) {
        CollectionReference ref = conn.collection("storages");
        ref
                .document(storage.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("dStor", "Document successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("dStor", "Error deleting document", e);
                    }
                });
    }

    public void newIngredient(Context context, Ingredient ingredient) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("amount", ingredient.getAmount());
        data.put("cost", ingredient.getCost());
        data.put("date", ingredient.getDate());
        data.put("location", ingredient.getLoc());
        data.put("user", getUsername());
        CollectionReference ref = conn.collection("user_ingredients");
        store(ref, "Ingredient", data, context);
    }

    public List<Ingredient> getIngredients(Context context) {
        List<Ingredient> list = new ArrayList<Ingredient>(){};
        CollectionReference ref = conn.collection("user_ingredients");
        ref
                .whereEqualTo("user", getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Integer amount = document.getLong("amount").intValue();
                                Integer cost = document.getLong("cost").intValue();
                                Date date = document.getDate("date");
                                String locRef = document.getString("location");
                                //Ingredient ingredient = new Ingredient(amount, cost, date, locRef);
                                //list.add(ingredient);
                            }
                            Log.d("gStor", "Documents successfully queried", task.getException());
                        } else {
                            Log.d("gStor", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
    }

    /**
     * Used to add Meals to user's database
     * @param meal : A meal of type {@link Meal} containing information to be added to the database.
     * @param context : Activity {@link Context} in which this method is called. It is used to display {@link Toast} notification to user about success.
     */
    public void addMeal(Meal meal, Context context) {
        HashMap<String, Object> data = meal.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_meals = this.getConn().collection("user_meals");
        store(user_meals, "Meal", data, context);
    }

    public void modifyMeal(Meal new_meal, Context context) {
        HashMap<String, Object> data = new_meal.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_meals = this.getConn().collection("user_meals");

        try {
            DocumentReference doc = user_meals.document(new_meal.getM_id());
            doc.update(data);
            Toast.makeText(context, "Meal updated successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void removeMeal(Meal meal, Context context) {

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
        store(user_mealplans, "Meal Plan", data, context);
    }

    public void modifyMealPlan(MealPlan mealPlan, Context context) {

        HashMap<String, Object> data = mealPlan.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_mealplans = this.getConn().collection("user_mealplans");

        try {
            DocumentReference doc = user_mealplans.document(mealPlan.get_ump_id());
            doc.update(data);
            Toast.makeText(context, "Mealplan updated successfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public void removeMealPlan(MealPlan mealPlan, Context context) {

    }

    public void addRecipe(Recipe recipe, Context context) {
        HashMap<String, Object> data = recipe.getStorable();
        data.put("user", this.getUsername());
        CollectionReference user_recipes = this.getConn().collection("user_recipes");
        store(user_recipes, "Recipes", data, context);
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
