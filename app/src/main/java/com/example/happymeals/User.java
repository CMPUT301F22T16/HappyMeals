package com.example.happymeals;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class User{
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
        ref
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("store", "Data has been added successfully!");
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

    public void newStorage(Context context, Storage storage) {
        HashMap<String, String> data = new HashMap<>();
        data.put("type", storage.getStorName());
        data.put("user", getUsername());
        CollectionReference ref = conn.collection("storages");
        ref
                .document(storage.getId())
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
                        Toast.makeText(context, "Storage not be added", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public List<Storage> getStorages(Context context) {
        List<Storage> list = new ArrayList<Storage>(){};
        CollectionReference ref = conn.collection("storages");
        ref
                .whereEqualTo("user", getUsername())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Storage stor = new Storage(document.getString("type"), document.getId());
                                list.add(stor);
                            }
                            Log.d("gStor", "Documents successfully queried", task.getException());
                        } else {
                            Log.d("gStor", "Error getting documents: ", task.getException());
                            Toast.makeText(context,  "There was an error in retrieving data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return list;
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
                                Ingredient ingredient = new Ingredient(amount, cost, date, locRef);
                                list.add(ingredient);
                            }
                            Log.d("gStor", "Documents successfully queried", task.getException());
                        } else {
                            Log.d("gStor", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return list;
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
        store(user_meals, "Meal", data, context);
    }

//    public void addMealPlan(User user, MealPlan mealplan, ) {
//
//    }

}
