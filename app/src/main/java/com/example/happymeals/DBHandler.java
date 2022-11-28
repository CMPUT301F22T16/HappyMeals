package com.example.happymeals;

import static java.lang.Boolean.FALSE;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.ingredient.UserIngredient;
import com.example.happymeals.meal.MPMealRecipeListAdapter;
import com.example.happymeals.meal.MPMyMealsAdapter;
import com.example.happymeals.meal.MPPickRecipeListAdapter;
import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MPListAdapter;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.shoppinglist.SLMealPlanAdapter;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeIngredient;
import com.example.happymeals.storage.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
public class DBHandler implements Serializable{
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


    public void setSort(ArrayAdapter adapter, @Nullable List<UserIngredient> ilist, @Nullable List<Recipe> rlist,  @Nullable List<RecipeIngredient> sllist, String type) {
        String field;
        if (ilist != null) {
            field = "ing_sort";
        }
        else if (rlist != null) {
            field = "rec_sort";
        }
        else {
            field = "sl_sort";
        }
        DocumentReference doc = conn.collection("users").document(getUsername());
        doc
                .update(field, type)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (ilist != null) {
                            if (type.equals("A-Z")) {
                                Collections.sort(ilist, (o1, o2) -> (o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase())));
                            }
                            else if (type.equals("Z-A")) {
                                Collections.sort(ilist, (o1, o2) -> (o2.getDescription().toLowerCase().compareTo(o1.getDescription().toLowerCase())));
                            }
                            else if (type.equals("1-9")) {
                                Collections.sort(ilist, (o1, o2) -> o1.getCost().compareTo(o2.getCost()));
                            }
                            else if (type.equals("9-1")) {
                                Collections.sort(ilist, (o1, o2) -> o2.getCost().compareTo(o1.getCost()));
                            }
                        }
                       else if (rlist != null) {
                            if (type.equals("S1-9")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getNum_servings() - o2.getNum_servings()));
                            } else if (type.equals("S9-1")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getNum_servings() - o1.getNum_servings()));
                            } else if (type.equals("P1-9")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getPreparation_time() - o2.getPreparation_time()));
                            } else if (type.equals("P9-1")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getPreparation_time() - o1.getPreparation_time()));
                            }
                            else if (type.equals("CA-Z")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase())));
                            } else if (type.equals("CZ-A")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getCategory().toLowerCase().compareTo(o1.getCategory().toLowerCase())));
                            } else if (type.equals("TA-Z")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase())));
                            } else if (type.equals("TZ-A")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase())));
                            }
                        }
                        else {
                            if (type.equals("A-Z")) {
                                Collections.sort(sllist, (o1, o2) -> (o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase())));
                            }
                            else if (type.equals("Z-A")) {
                                Collections.sort(sllist, (o1, o2) -> (o2.getDescription().toLowerCase().compareTo(o1.getDescription().toLowerCase())));
                            }
                        }

                        adapter.notifyDataSetChanged();

                    }
                });

    }
    
    private void sortFilter(ArrayAdapter adapter, @Nullable List<UserIngredient> ilist, @Nullable List<Recipe> rlist, @Nullable List<RecipeIngredient> sllist) {
        DocumentReference doc = conn.collection("users").document(getUsername());
        doc
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String type;
                        adapter.clear();
                        if (ilist != null) {
                            type = task.getResult().getString("ing_sort");
                            if (type.equals("A-Z")) {
                                Collections.sort(ilist, (o1, o2) -> (o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase())));
                            } else if (type.equals("Z-A")) {
                                Collections.sort(ilist, (o1, o2) -> (o2.getDescription().toLowerCase().compareTo(o1.getDescription().toLowerCase())));
                            } else if (type.equals("1-9")) {
                                Collections.sort(ilist, (o1, o2) -> o1.getCost().compareTo(o2.getCost()));
                            } else if (type.equals("9-1")) {
                                Collections.sort(ilist, (o1, o2) -> o2.getCost().compareTo(o1.getCost()));
                            }
                            adapter.addAll(ilist);
                        }
                        else if (rlist != null) {
                            type = task.getResult().getString("rec_sort");
                            if (type.equals("S1-9")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getNum_servings() - o2.getNum_servings()));
                            } else if (type.equals("S9-1")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getNum_servings() - o1.getNum_servings()));
                            } else if (type.equals("P1-9")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getPreparation_time() - o2.getPreparation_time()));
                            } else if (type.equals("P9-1")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getPreparation_time() - o1.getPreparation_time()));
                            } else if (type.equals("CA-Z")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getCategory().toLowerCase().compareTo(o2.getCategory().toLowerCase())));
                            } else if (type.equals("CZ-A")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getCategory().toLowerCase().compareTo(o1.getCategory().toLowerCase())));
                            } else if (type.equals("TA-Z")) {
                                Collections.sort(rlist, (o1, o2) -> (o1.getTitle().toLowerCase().compareTo(o2.getTitle().toLowerCase())));
                            } else if (type.equals("TZ-A")) {
                                Collections.sort(rlist, (o1, o2) -> (o2.getTitle().toLowerCase().compareTo(o1.getTitle().toLowerCase())));
                            }
                            adapter.addAll(rlist);
                        }
                        else {
                            type = task.getResult().getString("sl_sort");
                            if (type.equals("A-Z")) {
                                Collections.sort(sllist, (o1, o2) -> (o1.getDescription().toLowerCase().compareTo(o2.getDescription().toLowerCase())));
                            } else if (type.equals("Z-A")) {
                                Collections.sort(sllist, (o1, o2) -> (o2.getDescription().toLowerCase().compareTo(o1.getDescription().toLowerCase())));
                            }
                            adapter.addAll(sllist);
                        }
                        adapter.notifyDataSetChanged();

                    }});
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

    public void checkIncomplete(Context context) {
        CollectionReference ref = conn.collection("user_ingredients");
        Query query = ref
                .whereEqualTo("user", getUsername())
                .whereEqualTo("incomplete", true);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> value) {
                        List<DocumentSnapshot> ingredients = value.getResult().getDocuments();
                        Log.d("Momo", String.valueOf(ingredients));
                        if (!ingredients.isEmpty()) {
                            Toast.makeText(context, "You have missing information in your ingredients!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void newUser(String userId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("ing_sort", "A-Z");
        data.put("rec_sort", "TA-Z");
        data.put("sl_sort", "A-Z");
        data.put("sl_incomplete", false);
        DocumentReference doc = conn.collection("users").document(userId);
        store(doc, data, "User");
    }


    public void validateUser(FirebaseUser user, Context context) {
        DocumentReference docRef = conn.collection("users").document(user.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (!documentSnapshot.exists()) {
                            newUser(user.getUid());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    //-------------------------------------------------Storage Methods-------------------------------------------------//

    /**
     * Fetches all the storages for the userin the Storage Activity
     * @param adapter
     */
    public void getStorages(ArrayAdapter<Storage> adapter) {
        CollectionReference ref = conn.collection("storages");
        Query query = ref.whereEqualTo("user", getUsername());
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        adapter.clear();
                        for (DocumentSnapshot snapshot: value) {
                            String type = snapshot.getString("type");

                            Storage storage = new Storage(type);
                            String id = snapshot.getId();
                            storage.setId(id);

                            int count = snapshot.getLong("items").intValue();
                            storage.setItemCount(count);

                            adapter.add(storage);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }

    /**
     * Fetches all the storage {@link String} types for the user in the User Ingredient Activity
     * @param adapter
     */
    public void getStorageTypes(ArrayList<String> adapter, boolean fragment) {
        CollectionReference ref = conn.collection("storages");
        Query query = ref.whereEqualTo("user", getUsername());
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        adapter.clear();
                        if (fragment == false){
                            adapter.add("Select location");
                        }

                        for (DocumentSnapshot snapshot: value) {
                            String type = snapshot.getString("type");

                            adapter.add(type);
                        }
                        if (fragment == FALSE){
                            adapter.add("Add new location");
                        }

                        // adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Stores a {@link Storage} object in the database, and attaches the database ID of the entry to the object.
     * @param storage : Object of type {@link Storage} that will be stored in the database.
     */
    public void addStorage(Storage storage) {
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
    public void getIngredients(ArrayAdapter adapter) {
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
                        Boolean status = doc.getBoolean("incomplete");
                        UserIngredient userIngredient = new UserIngredient(category, description, amount, cost, date, location, unit);
                        userIngredient.setIncomplete(status);
                        userIngredient.setId(doc.getId());
                        userIngredients.add(userIngredient);
                    }
                    sortFilter(adapter, userIngredients, null, null);
                    Log.d("uIng", "Local ingredients updated successfully!");
                }
            }
        });
    }

    /**
     * Keeps checking for changes in a user's query for user_ingredients and updates their ingredients if change is found.
     */
    public void getIngredientsForStorage(ArrayAdapter adapter, Storage storage) {

        Query query = conn.collection("user_ingredients").whereEqualTo("user", getUsername()).whereEqualTo("location", storage.getStoreName());
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d("uIng", "An error has occurred while trying to update the local ingredients");
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
                        int items = storage.getItemCount();
                        storage.setItemCount(items + 1);
                        adapter.add(userIngredient);
                    }
                    adapter.notifyDataSetChanged();
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
                        List<Recipe> rlist = new ArrayList<>();
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
                            recipe.setRId(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            rlist.add(recipe); // Adding the recipe from FireStore
                        }
                        sortFilter(adapter, null, rlist, null);
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
                            recipe.setRId(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            adapter.add(recipe); // Adding the recipe from FireStore
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Used to fetch either all Recipes for current user.
     * Attaches an event listener to the user's recipe documents in user_recipes Collection update their respective
     * ArrayAdapter and notify them for future real time updates.
     *
     * @param adapter {@link MPMealRecipeListAdapter} in which data is to be populated
     * @param dialog {@link LoadingDialog} dialog box to be suspended when data has been fetched.
     *
     * @throws RuntimeException if Recipe has no ingredients
     */
    public void getUserRecipesMealRecipeList(MPMealRecipeListAdapter adapter, LoadingDialog dialog) {
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
                            recipe.setRId(id);

                            String uri = (String) data.get("uri");
                            recipe.setDownloadUri(uri);

                            adapter.addToUserRecipes(recipe); // Adding the recipe from FireStore
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
        dialog.dismissDialog();
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
                            recipe.setRId(id);

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
        recipe.setRId(id);
    }

    /**
     * Update/edit a recipe in database
     * @param new_recipe {@link Recipe} to be updated.
     */
    public void updateRecipe(Recipe new_recipe) {
        HashMap<String, Object> data = new_recipe.getStorable();
        data.put("user", getUsername());
        CollectionReference user_recipes = getConn().collection("user_recipes");
        String id = new_recipe.getRId();
        update(user_recipes, id, data, "user_recipes");
    }

    /**
     * Delete a recipe in database
     * @param recipe {@link Recipe} to be deleted.
     */
    public void removeRecipe(Recipe recipe) {
        CollectionReference user_recipes = getConn().collection("user_recipes");
        String id = recipe.getRId();
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

                            meal.setMId(m_id);
                            tempMeals.add(meal);
                        }
                        for (String i : meal_ids) {
                            for (Meal meal : tempMeals) {
                                String j = meal.getMId();
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
                            meal.setMId(m_id);
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
        meal.setMId(id);
    }

    /**
     * Update/edit a meal in database
     * @param new_meal {@link Meal} to be updated.
     */
    public void modifyMeal(Meal new_meal) {
        HashMap<String, Object> data = new_meal.getStorable();
        data.put("user", getUsername());
        CollectionReference user_meals = getConn().collection("user_meals");
        String id = new_meal.getMId();
        update(user_meals, id, data, "user_meals");
    }

    /**
     * Delte a meal in database
     * @param meal {@link Meal} to be deleted.
     */
    public void removeMeal(Meal meal) {
        CollectionReference user_meals = getConn().collection("user_meals");
        String id = meal.getMId();
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
                            mealPlan.setUmpId(ump_id);

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
        mealplan.setUmpId(id);
    }

    /**
     * Update/edit a meal plan in database
     * @param mealPlan {@link MealPlan} to be updated.
     */
    public void updateMealPlan(MealPlan mealPlan) {

        HashMap<String, Object> data = mealPlan.getStorable();
        data.put("user", getUsername());
        CollectionReference user_mealplans = getConn().collection("user_mealplans");
        String id = mealPlan.getUmpId();
        update(user_mealplans, id, data, "user_mealplans");
    }

    /**
     * Delet a meal plan in database
     * @param mealPlan {@link MealPlan} to be deleted.
     */
    public void removeMealPlan(MealPlan mealPlan) {
        CollectionReference user_mealplans = getConn().collection("user_mealplans");
        String id = mealPlan.getUmpId();
        delete(user_mealplans, id, "user_mealplans");
    }

    /**
     * Used to fetch all MealPlans for current user.
     *
     * @param adapter
     * @param dialog
     */

    //-------------------------------------------------ShoppingList Methods-------------------------------------------------//

    /**
     * Get meal pla
     * @param adapter
     * @param dialog
     */
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
                            mealPlan.setUmpId(ump_id);

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
                    ArrayList<RecipeIngredient> slIngredients = UnitConverter.getShoppingList(mealPlan, userIngredients);
                    sortFilter(adapter, null, null, slIngredients);
                    Log.d("uIng", "Local ingredients updated successfully!");
                }
            }
        });
    }

    /**
     * Add an item from shopping list into ingredient.
     * @param adapter {@link ArrayAdapter} in which data is to be populated
     * @param description {@link String} description of item.
     * @param category {@link String} category of item.
     * @param needed {@link Integer}amount needed for given recipe.
     * @param unit {@link String} unit of item.
     * @param position {@link Integer} Item position in data list.
     */
    public void pickUpIngredient(ArrayAdapter adapter, String description, String category, double needed, String unit, Integer position) {

        CollectionReference ref = conn.collection("user_ingredients");

        Query query = ref
                .whereEqualTo("user", getUsername())
                .whereEqualTo("description", description)
                .whereEqualTo("category", category);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> value) {
                        List<DocumentSnapshot> ingredients = value.getResult().getDocuments();
                        DocumentSnapshot doc;
                        Double cost;
                        Date date;
                        Double amount;
                        if (ingredients.isEmpty()) {
                            cost = 0.0;
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, 2099);
                            cal.set(Calendar.MONTH, 11);
                            cal.set(Calendar.DAY_OF_MONTH, 30);
                            date = cal.getTime();
                            UserIngredient userIngredient = new UserIngredient(category, description, needed, cost, date, "", unit);
                            userIngredient.setIncomplete(true);
                            newIngredient(userIngredient);
                        }
                        else {
                            doc = ingredients.get(0);
                            cost = doc.getDouble("cost");
                            date = doc.getDate("date");
                            amount = doc.getDouble("amount");
                            UserIngredient userIngredient = new UserIngredient(category, description, amount+needed, cost, date, "", unit);
                            userIngredient.setId(doc.getId());
                            userIngredient.setIncomplete(true);
                            updateIngredient(userIngredient);
                        }

                        adapter.remove(position);
                        adapter.notifyDataSetChanged();
                        Log.d("uIng", "Local ingredients updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sling", "Updating ingredient as a result of a shopping list pickup has failed");
                    }
                });

    }
}
