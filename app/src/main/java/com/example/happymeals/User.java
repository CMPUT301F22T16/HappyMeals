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
    private String u_id;
    private String username;
    private FirebaseFirestore conn;


    private String get_u_id(String username) {
        // Search up database to get the u_id for the user with username = 'username'
        return "";
    }

    public User() {
        u_id = "Fw8E3ba2rjfSmuOPxxHE"; // Default generated right now (temporary)
        username = "Guest"; // This is temporary
        conn = FirebaseFirestore.getInstance();
    }

    public User(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
        u_id = this.get_u_id(this.username);
    }

    public String getU_id() {
        return this.u_id;
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

    public void addMeal(User user, Meal meal, Context context) {
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

        FirebaseFirestore conn = user.getConn();
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

//    public void addMealPlan(User user, MealPlan mealplan, ) {
//
//    }

}
