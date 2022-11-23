package com.example.happymeals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.meal.MPMealRecipeList;
import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.meal.MPPickRecipeActivity;
import com.example.happymeals.recipe.EditRecipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class NewMealEntryTests {
    private Solo solo;
    private DBHandler db;
    private FirebaseFirestore conn;
    private String recipe_id;
    private String meal_id;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * Create a recipe and insert it to firebase
     * so that it can be used in the tests
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        conn = FirebaseFirestore.getInstance();
        Recipe recipe = new Recipe("Testing Recipe", 10, 1, "Test", new ArrayList<>(), new ArrayList<>());
        HashMap<String, Object> data = recipe.getStorable();
        data.put("user", "Guest");
        CollectionReference user_recipes = conn.collection("user_recipes");
        DocumentReference doc = user_recipes.document();
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
        recipe_id=doc.getId();
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * This test ensure that user be able to create
     * a new meal with selecting meal to it
     */
    @Test
    public void testCreateNewMeal() throws InterruptedException {
        // starting from main activity
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("MEALS");
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);

        // click to add a new meal
        Button addButton = (Button) solo.getView(R.id.my_meals_add_button);
        solo.clickOnView(addButton);

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button addRecipeButton = (Button) solo.getView(R.id.mp_recipe_add_button);
        solo.clickOnView(addRecipeButton);
        solo.waitForDialogToOpen();
        TextView add_new_recipe = (TextView) solo.getView(R.id.bottom_sheet_textview1);
        solo.clickOnView(add_new_recipe);
        solo.assertCurrentActivity("Wrong Activity", MPPickRecipeActivity.class); // within pick

        solo.clickOnText("Testing Recipe");
        solo.clickOnButton("CONFIRM");

        // ensure the recipe was added to the meal
        solo.waitForActivity(MPMealRecipeList.class);
        solo.waitForText("Testing Recipe");

        // ensure the meal was added to meal list
        CollectionReference user_meals = conn.collection("user_meals");
        List<String> recipe_ids = new ArrayList<>();
        recipe_ids.add(recipe_id);
        Query query = user_meals.whereIn(com.google.firebase.firestore.FieldPath.documentId(), recipe_ids);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                assertTrue(task.isSuccessful());
                for (DocumentSnapshot document : task.getResult()) {
                    assertTrue(document.exists());
                }

            }
        });
        Button cancelButton = (Button) solo.getView(R.id.mpmeal_recipe_list_cancel);
        solo.clickOnView(cancelButton);
        solo.clickOnText("OK");
        solo.waitForActivity(MPMyMealsActivity.class);

        Button finishButton = (Button) solo.getView(R.id.my_meals_finish);
        solo.clickOnView(finishButton);
    }

    @Test
    public void test_create_new_recipes(){
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnButton("MEALS");
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);

        // click to add a new meal
        Button addButton = (Button) solo.getView(R.id.my_meals_add_button);
        solo.clickOnView(addButton);

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button addRecipeButton = (Button) solo.getView(R.id.mp_recipe_add_button);
        solo.clickOnView(addRecipeButton);
        solo.waitForDialogToOpen();
        solo.clickOnText("Create New Recipe and Add to Meal");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button cancelButton = (Button) solo.getView(R.id.mpmeal_recipe_list_cancel);
        solo.clickOnView(cancelButton);
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);


    }




    /**
     * runs after all tests
     * delete the test recipes we added before
     * from firebase
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();

        CollectionReference user_recipes = conn.collection("user_recipes");
        CollectionReference user_meals = conn.collection("user_meals");

        user_recipes
                .document(recipe_id)
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

}
