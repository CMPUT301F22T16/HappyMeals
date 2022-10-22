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
    CollectionReference userRef;

    public User() {
        username = "Guest"; // This is temporary
        conn = FirebaseFirestore.getInstance();
        userRef = conn.collection("users");
    }

    public User(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
    }

    public void newUser(String username) {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", username);
        userRef.add(data);
    }

    public String getUsername() {
        return this.username;
    }

    public FirebaseFirestore getConn() {
        return this.conn;
    }

//    public static User createUser(String username) {
//        // Create a new user in the database
//        // Create a User object for that user
//        // Return that user
//
//
//    }

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

        FirebaseFirestore conn = this.getConn();
        CollectionReference user_meals = conn.collection("user_meals");
        String TAG = "User";
        user_meals
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Data has been added successfully!");
                        Toast.makeText(context, "Meal added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added!" + e.toString());
                        Toast.makeText(context, "Meal could not be added", Toast.LENGTH_SHORT).show();
                    }
                });
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

        FirebaseFirestore conn = this.getConn();
        CollectionReference user_mealplans = conn.collection("user_mealplans");
        String TAG = "User";

        user_mealplans
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Data has been added successfully!");
                        Toast.makeText(context, "Meal plan created successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data could not be added!" + e.toString());
                        Toast.makeText(context, "Meal plan could not be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
