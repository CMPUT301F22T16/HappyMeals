package com.example.happymeals;

import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.meal.MPMyMealsAdapter;
import com.example.happymeals.meal.MPPickRecipeListAdapter;
import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MPListAdapter;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.shoppinglist.SLMealPlanAdapter;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Represents a DBHandler object. This class maintains a username and also has a database connection established that it can use to add/modify/delete
 * content of following collections in Firestore
 * 1. user_ingredients: {@link UserIngredient} is model class for this.
 * 2. users: {@link User} is model class for this.
 * 3. user_storages: {@link Storage} is the model class for this.
 * 4. user_recipes: {@link Recipe} is the model class for this.
 * 5. user_meals: {@link Meal} is the model class for this.
 * 6. user_mealplans: {@link MealPlan} is the model class for this.
 *
 */
public class DBHandler {
    /**
     * Members
     *  username: A {@link String} username that represents the document id of the user document in database.
     *  conn: A {@link FirebaseFirestore} database connection to add/modify/delete/query data upon request.
     *  storage: A{@link FirebaseStorage} connection to storage where images are to be stored.
     */
    private String username;
    private FirebaseFirestore conn;
    private FirebaseStorage storage;

    public DBHandler(String username) {
        // login existing user
        this.username = username;
        conn = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public String getUsername() {
        return this.username;
    }

    private FirebaseFirestore getConn() {
        return this.conn;
    }

    /**
     * Generic store method that can be used to store data to firestore.
     * @param doc {@link DocumentReference} reference to the firestore document.
     * @param data {@link HashMap<String, Object>} data to be stored as keys and values.
     * @param collType {@link String} the type of the collection.
     * @return {@link String} the document id of the newly stored object in Firestore.
     */
    private String store(DocumentReference doc, HashMap data, String collType) {
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
                    }
                });

        return doc.getId();
    }

    /**
     * Generic delete method used to delete any document with the provided id.
     * @param ref A {@link CollectionReference} reference to the collection in which the document is to be deleted.
     * @param id {@link String} id of the document to be deleted.
     * @param collType {@link String} Collection type.
     */
    private void delete(CollectionReference ref, String id, String collType) {
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
                    }
                });
    }

    /**
     * Generic update method used to update/edit any document with the provided id.
     * @param ref A {@link CollectionReference} reference to the collection in which the document is to be updated.
     * @param id {@link String} id of the document to be updated.
     * @param data {@link HashMap<String, Object>} is new data to be changed at documents place.
     * @param collType {@link String} Collection type.
     */
    private void update(CollectionReference ref, String id, HashMap<String, Object> data, String collType) {
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
                    }
                });
    }

    //---------------------------------------------------User Methods--------------------------------------------------//

    public void newUser(String userId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ing_sort", "A-Z");
        DocumentReference doc = conn.collection("users").document(userId);
        store(doc, data, "User");
    }

    //-------------------------------------------------Storage Methods-------------------------------------------------//

    /**
     * Stores a {@link Storage} object in the database, and attaches the database ID of the entry to the object.
     * @param storage : Object of type {@link Storage} that will be stored in the database.
     */
    public void newStorage(Storage storage) {
        HashMap<String, Object> data = storage.getStorable();
        data.put("user", getUsername());
        DocumentReference doc = conn.collection("storages").document();
        String id = store(doc, data, "Storage");
        storage.setId(id);
    }

    /**
     * Deletes the entry of a {@link Storage} object from the database.
     * @param storage : Object of type {@link Storage} that will be removed from the database
     */
    public void deleteStorage(Storage storage) {
        CollectionReference ref = conn.collection("storages");
        delete(ref, storage.getId(), "Storage");
    }

    /**
     * Updates the entry of {@link Storage} object in the database.
     * @param storage : Object of type {@link Storage} to be updated in the database.
     */
    public void updateStorage(Storage storage) {
        CollectionReference ref = conn.collection("storages");
        update(ref, storage.getId(), storage.getStorable(), "Storage");
    }

    //-------------------------------------------------Ingredient Methods-------------------------------------------------//

    /**
     * Stores a {@link UserIngredient} object in the database, and attaches the database ID of the entry to the object.
     * @param userIngredient : Object of type {@link Storage} that will be added to the database
     */
    public void newIngredient(UserIngredient userIngredient) {
        HashMap<String, Object> data  = userIngredient.getStorable();
        data.put("user", getUsername());
        DocumentReference doc = conn.collection("user_ingredients").document();
        String id = store(doc, data, "Ingredient");
        userIngredient.setId(id);
    }

    /**
     * Deletes the entry of a {@link UserIngredient} object from the database.
     * @param userIngredient : Object of type {@link UserIngredient} that will be removed from the database
     */
    public void deleteIngredient(UserIngredient userIngredient) {
        CollectionReference ref = conn.collection("user_ingredients");
        delete(ref, userIngredient.getId(), "Ingredient");
    }

    /**
     * Updates the entry of {@link UserIngredient} object in the database.
     *
     * @param userIngredient : Object of type {@link UserIngredient} to be updated in the database.
     */
    public void updateIngredient(UserIngredient userIngredient) {
        CollectionReference ref = conn.collection("user_ingredients");
        update(ref, userIngredient.getId(), userIngredient.getStorable(), "Ingredient");
    }

    /**
     * Keeps checking for changes in a user's query for user_ingredients and updates their ingredients if change is found.
     */
    public void getIngredients(ArrayAdapter adapter, @Nullable TextView totalCost) {
        Query query = conn.collection("user_ingredients").whereEqualTo("user", getUsername());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("uIng", "An error has occured while trying to update the local ingredients");
                } else {
                    List<UserIngredient> userIngredients = new ArrayList<>();
                    Double sum = 0.0;
                    adapter.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String category = doc.getString("category");
                        String description = doc.getString("description");
                        Double amount = doc.getDouble("amount");
                        Double cost = doc.getDouble("cost");
                        sum += cost * amount;
                        Date date = doc.getDate("date");
                        String location = doc.getString("location");
                        String unit = doc.getString("unit");
                        UserIngredient userIngredient = new UserIngredient(category, description, amount, cost, date, location, unit);
                        userIngredient.setId(doc.getId());
                        userIngredients.add(userIngredient);
                        adapter.add(userIngredient);
                    }
                    adapter.notifyDataSetChanged();
                    if (!Objects.isNull(totalCost)) {
                        totalCost.setText("Total cost: $" + String.valueOf(sum));
                    }
                    Log.d("uIng", "Local ingredients updated successfully!");
                }
            }
        });
    }

    //-------------------------------------------------Recipe Methods-------------------------------------------------//

    /**
     * Used to fetch either all Recipes for current user.
     * Attaches an event listener to the user's recipe documents in user_recipes Collection update their respective
     * ArrayAdapter and notify them for future real time updates.
     *
     * @param adapter {@link ArrayAdapter} in which data is to be populated
     * @param dialog {@link LoadingDialog} dialog box to be suspended when data has been fetched.
     *
     * @throws RuntimeException if Recipe has no ingredients
     */
    public void getUserRecipes(ArrayAdapter adapter, LoadingDialog dialog) {
        CollectionReference ref = conn.collection("user_recipes");
        Query query = ref.whereEqualTo("user", getUsername());

        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH RECIPES", error.toString());
                            return;
                        }

                        adapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String id = doc.getId();
                            Map<String, Object> data = doc.getData();
                            String category = (String) data.get("category");
                            List<String> comments = (List<String>) doc.get("comments");


                            Integer num_servings = ((Long) data.get("num_servings")).intValue();
                            Integer preparation_time = ((Long) data.get("preparation_time")).intValue();
                            String title = (String) data.get("title");


                            Map<String, Map<String, Object>> ingredients = (Map<String, Map<String, Object>>) data.get("ingredients");
                            List<RecipeIngredient> recipeIngredients = new ArrayList<>();

                            if (ingredients == null) {
                                throw new RuntimeException("Ingredients not found");
                            }

                            for (String desc : ingredients.keySet()) {
                                Map<String, Object> info = ingredients.get(desc);
                                Double amount = (Double) info.get("amount");
                                String ingredientCategory = (String) info.get("category");
                                String units = (String) info.get("units");

                                RecipeIngredient recipeIngredient = new RecipeIngredient(desc, ingredientCategory, amount);
                                recipeIngredient.setUnit(units);

                                recipeIngredients.add(recipeIngredient);
                            }


                            Recipe recipe = new Recipe(title, preparation_time, num_servings, category, comments, recipeIngredients);
                            recipe.setR_id(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            adapter.add(recipe); // Adding the recipe from FireStore
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
        dialog.dismissDialog();
    }

    /**
     * Get all recipes for the user associated to their meals
     * @param adapter {@link ArrayAdapter} in which data is to be populated
     * @param dialog {@link LoadingDialog} dialog box to be suspended when data has been fetched.
     */
    public void getUserRecipesForMeals(MPPickRecipeListAdapter adapter, LoadingDialog dialog) {
        CollectionReference ref = conn.collection("user_recipes");
        Query query = ref.whereEqualTo("user", getUsername());
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH RECIPES", error.toString());
                            return;
                        }

                        adapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String id = doc.getId();
                            Map<String, Object> data = doc.getData();
                            String category = (String) data.get("category");
                            List<String> comments = (List<String>) doc.get("comments");


                            Map<String, Map<String, Object>> ingredients = (Map<String, Map<String, Object>>) data.get("ingredients");
                            List<RecipeIngredient> recipeIngredients = new ArrayList<>();

                            for (String desc : ingredients.keySet()) {
                                Map<String, Object> info = ingredients.get(desc);
                                Double amount = (Double) info.get("amount");
                                String ingredientCategory = (String) info.get("category");
                                String units = (String) info.get("units");

                                RecipeIngredient recipeIngredient = new RecipeIngredient(desc, ingredientCategory, amount);
                                recipeIngredient.setUnit(units);
                                recipeIngredients.add(recipeIngredient);
                            }


                            Integer num_servings = ((Long) data.get("num_servings")).intValue();
                            Integer preparation_time = ((Long) data.get("preparation_time")).intValue();
                            String title = (String) data.get("title");

                            Recipe recipe = new Recipe(title, preparation_time, num_servings, category, comments, recipeIngredients);
                            recipe.setR_id(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            adapter.add(recipe); // Adding the recipe from FireStore
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }


    /**
     * Used to get user recipes with ids in the recipe_ids {@link List<String>}. Puts the returned recipes
     * in the recipes {@link List<Recipe>}
     *
     * @param recipes A {@link List<Recipe>} to which recipes have to be added.
     * @param recipe_ids Fetched recipes have to be contained in {@link List<String>} ids.
     */
    private void getUserRecipesWithID(List<Recipe> recipes, List<String> recipe_ids) {
        if (recipe_ids.isEmpty()){return;}
        CollectionReference ref = conn.collection("user_recipes");
        Query query = ref.whereEqualTo("user", getUsername()).whereIn(com.google.firebase.firestore.FieldPath.documentId(), recipe_ids);
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH RECIPES", error.toString());
                            return;
                        }

                        recipes.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String id = doc.getId();
                            Map<String, Object> data = doc.getData();
                            String category = (String) data.get("category");
                            List<String> comments = (List<String>) data.get("comments");


                            Map<String, Map<String, Object>> ingredients = (Map<String, Map<String, Object>>) data.get("ingredients");
                            List<RecipeIngredient> recipeIngredients = new ArrayList<>();

                            for (String desc : ingredients.keySet()) {
                                Map<String, Object> info = ingredients.get(desc);
                                Double amount = (Double) info.get("amount");
                                String ingredientCategory = (String) info.get("category");
                                String units = (String) info.get("units");

                                RecipeIngredient recipeIngredient = new RecipeIngredient(desc, ingredientCategory, amount);
                                recipeIngredient.setUnit(units);
                                recipeIngredients.add(recipeIngredient);
                            }



                            Integer num_servings = ((Long) data.get("num_servings")).intValue();
                            Integer preparation_time = ((Long) data.get("preparation_time")).intValue();
                            String title = (String) data.get("title");

                            Recipe recipe = new Recipe(title, preparation_time, num_servings, category, comments, recipeIngredients);
                            recipe.setR_id(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            recipes.add(recipe); // Adding the recipe from FireStore
                        }
                    }
                });
    }


    /**
     * Add a recipe to database
     * @param recipe {@link Recipe} to be added.
     */
    public void addRecipe(Recipe recipe) {
        HashMap<String, Object> data = recipe.getStorable();
        data.put("user", getUsername());
        DocumentReference user_recipes = getConn().collection("user_recipes").document();
        String id = store(user_recipes, data, "Recipes");
        recipe.setR_id(id);
    }

    /**
     * Update/edit a recipe in database
     * @param new_recipe {@link Recipe} to be updated.
     */
    public void updateRecipe(Recipe new_recipe) {
        HashMap<String, Object> data = new_recipe.getStorable();
        data.put("user", getUsername());
        CollectionReference user_recipes = getConn().collection("user_recipes");
        String id = new_recipe.get_r_id();
        update(user_recipes, id, data, "user_recipes");
    }

    /**
     * Delete a recipe in database
     * @param recipe {@link Recipe} to be deleted.
     */
    public void removeRecipe(Recipe recipe) {
        CollectionReference user_recipes = getConn().collection("user_recipes");
        String id = recipe.get_r_id();
        delete(user_recipes, id, "user_recipes");

        // Removing photo
        String title = recipe.getTitle();
        StorageReference rootRef = storage.getReference();
        StorageReference photoRef = rootRef.child("images/" + this.getUsername() + "/" + title + ".jpg");
        photoRef.delete();
    }

    public void uploadImage(Uri uri, Recipe recipe, String filetype) {

        if (uri == null) {
            return;
        }

        StorageReference rootRef = storage.getReference();

        // TODO support other file formats

        StorageReference photoRef = rootRef.child("images/" + this.getUsername() + "/" + recipe.getTitle() + "." + filetype);

        UploadTask uploadTask = photoRef.putFile(uri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    recipe.setDownloadUri(downloadUri.toString());
                    updateRecipe(recipe);

                } else {
                    Log.d("UPLOAD", "Cannot upload image");
                }
            }
        });
    }


    //-------------------------------------------------Meal Methods-------------------------------------------------//

    /**
     * Used to fetch all Meals for current user.
     *
     * @param meals
     * @param dialog
     */
    private void getUserMealsWithID(List<Meal> meals, LoadingDialog dialog, List<String> meal_ids) {
        if(meal_ids.isEmpty()) {return;}
        CollectionReference ref = conn.collection("user_meals");
        ref
                .whereEqualTo("user", getUsername())
                .whereIn(com.google.firebase.firestore.FieldPath.documentId(), meal_ids)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH MEALS", error.toString());
                            return;
                        }

                        meals.clear();
                        List<Meal> tempMeals = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            String m_id = doc.getId();
                            Double cost = (Double) doc.getDouble("cost");

                            // Get the Recipes and scalings
                            Map<String, Double> recipe_scalings = (Map<String, Double>) doc.get("recipe_scalings");
                            String[] recipe_ids = new String[recipe_scalings.size()];
                            recipe_scalings.keySet().toArray(recipe_ids);
                            List<String> recipe_id_list = Arrays.asList(recipe_ids);
                            List<Recipe> recipes = new ArrayList<>();
                            getUserRecipesWithID(recipes, recipe_id_list);

                            String title = (String) doc.getString("title");
                            Meal meal = new Meal(title, recipes, recipe_scalings, cost);

                            meal.setM_id(m_id);
                            tempMeals.add(meal);
                        }
                        for (String i : meal_ids) {
                            for (Meal meal : tempMeals) {
                                String j = meal.getM_id();
                                if(i.equals(j)) {
                                    meals.add(meal.copy());
                                }
                            }
                        }
                    }
                });
    }

    /**
     * Used to fetch all Meals for current user.
     *
     * @param adapter Adapter to which the meals have to be added.
     * @param dialog {@link LoadingDialog} to be suspended when done fetching.
     */

    public void getUserMeals(MPMyMealsAdapter adapter, LoadingDialog dialog) {
        CollectionReference ref = conn.collection("user_meals");
        ref
                .whereEqualTo("user", getUsername())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH MEALS", error.toString());
                            return;
                        }
                        adapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            String m_id = doc.getId();
                            // Get the cost
                            Double cost = (Double) doc.getDouble("cost");

                            // Get the Recipes and scalings
                            Map<String, Double> recipe_scalings = (Map<String, Double>) doc.get("recipe_scalings");
                            List<Recipe> recipes = new ArrayList<>();
                            if (!recipe_scalings.keySet().isEmpty()) {
                                String[] recipe_ids = new String[recipe_scalings.size()];
                                recipe_scalings.keySet().toArray(recipe_ids);
                                List<String> recipe_id_list = Arrays.asList(recipe_ids);
                                getUserRecipesWithID(recipes, recipe_id_list);
                            }

                            String title = (String) doc.getString("title");
                            Meal meal = new Meal(title, recipes, recipe_scalings, cost);
                            meal.setM_id(m_id);
                            adapter.add(meal);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Add a meal to database
     * @param meal {@link Meal} to be added.
     */
    public void addMeal(Meal meal) {
        HashMap<String, Object> data = meal.getStorable();
        data.put("user", getUsername());
        DocumentReference user_meals = getConn().collection("user_meals").document();
        String id = store(user_meals, data, "Meal");
        meal.setM_id(id);
    }

    /**
     * Update/edit a meal in database
     * @param new_meal {@link Meal} to be updated.
     */
    public void modifyMeal(Meal new_meal) {
        HashMap<String, Object> data = new_meal.getStorable();
        data.put("user", getUsername());
        CollectionReference user_meals = getConn().collection("user_meals");
        String id = new_meal.getM_id();
        update(user_meals, id, data, "user_meals");
    }

    /**
     * Delte a meal in database
     * @param meal {@link Meal} to be deleted.
     */
    public void removeMeal(Meal meal) {
        CollectionReference user_meals = getConn().collection("user_meals");
        String id = meal.getM_id();
        delete(user_meals, id, "user_meals");
    }

    //-------------------------------------------------MealPlan Methods-------------------------------------------------//

    /**
     * Used to fetch all MealPlans for current user.
     *
     * @param adapter
     * @param dialog
     */

    public void getUserMealPlans(MPListAdapter adapter, LoadingDialog dialog) {

        CollectionReference ref = conn.collection("user_mealplans");
        ref
                .whereEqualTo("user", getUsername())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH MEALPLANS", error.toString());
                            return;
                        }

                        adapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            // Fetching id
                            String ump_id = doc.getId();

                            // Fetching all data
                            Map<String, Object> data = doc.getData();

                            // Fetching meal plan
                            List<Map<String, String>> plans = (List<Map<String, String>>) data.get("plans");

                            List<List<Meal>> mealplan = new ArrayList<>();

                            for (Map<String, String> dayMap : plans) {
                                List<String> meal_ids = new ArrayList<>();
                                List<Meal> meals = new ArrayList<>();
                                mealplan.add(meals);

                                for (String meal_id : dayMap.values()) {
                                    meal_ids.add(meal_id);
                                }

                                getUserMealsWithID(meals, dialog, meal_ids);

                            }

                            // Fetching number of days
                            Integer num_days = ((Long) data.get("num_days")).intValue();

                            // Fetching title
                            String title = doc.getString("title");

                            // Creating meal plan object
                            MealPlan mealPlan = new MealPlan(title, mealplan, num_days);

                            // Setting the document id
                            mealPlan.setUmp_id(ump_id);

                            // Adding the meal plan to the adapter
                            adapter.add(mealPlan);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Add a meal plan to database
     * @param mealplan {@link MealPlan} to be added.
     */
    public void addMealPlan(MealPlan mealplan) {
        HashMap<String, Object> data = mealplan.getStorable();
        data.put("user", getUsername());
        DocumentReference user_mealplans = getConn().collection("user_mealplans").document();
        String id = store(user_mealplans, data, "Meal Plan");
        mealplan.setUmp_id(id);
    }

    /**
     * Update/edit a meal plan in database
     * @param mealPlan {@link MealPlan} to be updated.
     */
    public void updateMealPlan(MealPlan mealPlan) {

        HashMap<String, Object> data = mealPlan.getStorable();
        data.put("user", getUsername());
        CollectionReference user_mealplans = getConn().collection("user_mealplans");
        String id = mealPlan.get_ump_id();
        update(user_mealplans, id, data, "user_mealplans");
    }

    /**
     * Delet a meal plan in database
     * @param mealPlan {@link MealPlan} to be deleted.
     */
    public void removeMealPlan(MealPlan mealPlan) {
        CollectionReference user_mealplans = getConn().collection("user_mealplans");
        String id = mealPlan.get_ump_id();
        delete(user_mealplans, id, "user_mealplans");
    }

    /**
     * Used to fetch all MealPlans for current user.
     *
     * @param adapter
     * @param dialog
     */

    //-------------------------------------------------ShoppingList Methods-------------------------------------------------//

    public void getSLMealPlans(SLMealPlanAdapter adapter, LoadingDialog dialog) {

        CollectionReference ref = conn.collection("user_mealplans");
        ref
                .whereEqualTo("user", getUsername())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.d("FETCH MEALPLANS", error.toString());
                            return;
                        }

                        adapter.clear();
                        for (QueryDocumentSnapshot doc : value) {
                            // Fetching id
                            String ump_id = doc.getId();

                            // Fetching all data
                            Map<String, Object> data = doc.getData();

                            // Fetching meal plan
                            List<Map<String, String>> plans = (List<Map<String, String>>) data.get("plans");

                            List<List<Meal>> mealplan = new ArrayList<>();

                            for (Map<String, String> dayMap : plans) {
                                List<String> meal_ids = new ArrayList<>();
                                List<Meal> meals = new ArrayList<>();
                                mealplan.add(meals);

                                for (String meal_id : dayMap.values()) {
                                    meal_ids.add(meal_id);
                                }

                                getUserMealsWithID(meals, dialog, meal_ids);

                            }

                            // Fetching number of days
                            Integer num_days = ((Long) data.get("num_days")).intValue();

                            // Fetching title
                            String title = doc.getString("title");

                            // Creating meal plan object
                            MealPlan mealPlan = new MealPlan(title, mealplan, num_days);

                            // Setting the document id
                            mealPlan.setUmp_id(ump_id);

                            // Adding the meal plan to the adapter
                            adapter.add(mealPlan);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Keeps checking for changes in a user's query for user_ingredients and updates their ingredients if change is found.
     */
    public void getSLIngredients(ArrayAdapter adapter, MealPlan mealPlan) {
        Query query = conn.collection("user_ingredients").whereEqualTo("user", getUsername());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("uIng", "An error has occured while trying to update the local ingredients");
                } else {
                    List<UserIngredient> userIngredients = new ArrayList<>();
                    Double sum = 0.0;
                    adapter.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        String category = doc.getString("category");
                        String description = doc.getString("description");
                        Double amount = doc.getDouble("amount");
                        Double cost = doc.getDouble("cost");
                        sum += cost * amount;
                        Date date = doc.getDate("date");
                        String location = doc.getString("location");
                        String unit = doc.getString("unit");
                        UserIngredient userIngredient = new UserIngredient(category, description, amount, cost, date, location, unit);
                        userIngredient.setId(doc.getId());
                        userIngredients.add(userIngredient);
                    }
                    List<RecipeIngredient> slIngredients = UnitConverter.getShoppingList(mealPlan, userIngredients);
                    adapter.addAll(slIngredients);
                    adapter.notifyDataSetChanged();
                    Log.d("uIng", "Local ingredients updated successfully!");
                }
            }
        });
    }
}
