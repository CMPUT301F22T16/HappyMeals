package com.example.happymeals;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class User {

    private String username;
    private FirebaseFirestore conn;

    public User() {
        username = "Guest"; // This is temporary
        conn = FirebaseFirestore.getInstance();
    }

    public User(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
    }

    public void newUser(String username) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        CollectionReference ref = this.conn.collection("users");
        ref.add(data);
    }


    private void store(CollectionReference ref, String collType, HashMap data, Context context) {
        ref
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("User", "Data has been added successfully!");
                        Toast.makeText(context,  collType + " added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("User", "Data could not be added!" + e.toString());
                        Toast.makeText(context, collType + " not be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void addStorage(Context context, Storage stor) {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", stor.getStorName());
        data.put("user", this.username);
        CollectionReference storRef = this.conn.collection("storages");
        store(storRef,"Storage", data, context);
    }

    public String getUsername() {
        return this.username;
    }

    public FirebaseFirestore getConn() {
        return this.conn;
    }

    public void addMeal(Meal meal, Context context) {
        List<Recipe> recipes = meal.getRecipes();
        List<Double> scalings = meal.getScalings();
        double cost = meal.getCost();
        HashMap<String, Object> data = new HashMap<>();
        data.put("cost", cost);
        data.put("scalings", scalings);
        List<String> recipe_ids = new ArrayList<>();
        for (Recipe recipe: recipes) {
            recipe_ids.add(recipe.get_r_id());
        }

        data.put("recipes", recipe_ids);
        CollectionReference user_meals = conn.collection("user_meals");
        store(user_meals, "Meal", data, context);
    }

    public void addMealPlan(MealPlan mealplan, Context context) {
        List<Meal> breakfast = mealplan.getBreakfast();
        List<Meal> lunch = mealplan.getLunch();
        List<Meal> dinner = mealplan.getDinner();
        int num_days = mealplan.getNum_days();
        List<String> breakfast_ids = new ArrayList<>();
        List<String> lunch_ids = new ArrayList<>();
        List<String> dinner_ids = new ArrayList<>();

        for (int i = 0; i < num_days; i++) {
            breakfast_ids.add(breakfast.get(i).getM_id());
            lunch_ids.add(lunch.get(i).getM_id());
            dinner_ids.add(dinner.get(i).getM_id());
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("breakfast", breakfast_ids);
        data.put("lunch", lunch_ids);
        data.put("dinner", dinner_ids);
        data.put("num_days", num_days);

        CollectionReference user_mealplans = conn.collection("user_mealplans");
        store(user_mealplans, "Meal Plan", data, context);
    }

}
