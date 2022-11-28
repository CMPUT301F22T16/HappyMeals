package com.example.happymeals;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.meal.Meal;
import com.example.happymeals.mealplan.MPMealListActivity;
import com.example.happymeals.mealplan.MPMealPlanActivity;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeIngredient;
import com.example.happymeals.shoppinglist.SLSelectMealPlanActivity;
import com.example.happymeals.shoppinglist.SLShoppingListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ShoppingListTest {
    private Solo solo;
    private FirebaseFirestore conn;
    private DBHandler db;
    private String mp_Id;
    private Recipe recipe;
    private Meal meal;
    private UserIngredient userIngredient;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * Create a mealPlan and insert it to firebase
     * go to Shopping List page
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        conn = FirebaseFirestore.getInstance();
        db = new DBHandler("Guest");
        RecipeIngredient recipeIngredient = new RecipeIngredient("Carrot", "Fruit", 100.0);
        recipeIngredient.setUnit("kg");
        List<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>();
        recipeIngredients.add(recipeIngredient);
        //set up recipe test data
        recipe = new Recipe("slTestRecipe", 10, 1, "Test", new ArrayList<>(), recipeIngredients);
        db.addRecipe(recipe);

        // set up meal test data
        meal = new Meal();
        meal.addRecipe(recipe);
        meal.setScalingForRecipe(recipe,2.0);
        meal.setTitle("slTestMeal");
        db.addMeal(meal);
        List<Meal> meals =new ArrayList<>();
        meals.add(meal);
        List<List<Meal>> listMeals = new ArrayList<>();
        listMeals.add(meals);

        // set up userIngredient test data
        Date date = new Date();
        date.setTime(date.getTime()+1000000000);
        userIngredient = new UserIngredient("Fruit", "Carrot",12.0,2.0, date, "mockLoc", "kg");
        userIngredient.setCost(1.0);
        db.newIngredient(userIngredient);
        MealPlan mealPlan = new MealPlan("slTestMealPlan", listMeals, 1);
        HashMap<String, Object> data = mealPlan.getStorable();
        data.put("user", "Guest");
        CollectionReference user_recipes = conn.collection("user_mealplans");
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
        mp_Id =doc.getId();

        solo.clickOnButton("Shopping List");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", SLSelectMealPlanActivity.class);
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
     * This test ensure that user can view a shopping list
     * generated based off a MealPlan
     */
    @Test
    public void testCreateNewMealPlan() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity",SLSelectMealPlanActivity.class);
        int mpBefore = ((RecyclerView) solo.getView(R.id.sl_select_mp_list)).getAdapter().getItemCount();
        solo.clickOnView(solo.getView(R.id.meal_plan_list_content_layout));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", SLShoppingListActivity.class);
        int ingredientCnt =((ListView) solo.getView(R.id.sl_shopping_list)).getAdapter().getCount();
        assertNotEquals(ingredientCnt, 0);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
