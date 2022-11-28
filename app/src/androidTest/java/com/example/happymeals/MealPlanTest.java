package com.example.happymeals;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.mealplan.MPMealListActivity;
import com.example.happymeals.mealplan.MPMealPlanActivity;
import com.example.happymeals.mealplan.MealPlan;
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

import java.util.HashMap;

@RunWith(AndroidJUnit4.class)
public class MealPlanTest {
    private Solo solo;
    private FirebaseFirestore conn;
    private String mp_Id;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * Create a mealPlan and insert it to firebase
     * go to MealPlan page
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        conn = FirebaseFirestore.getInstance();
        MealPlan mealPlan = new MealPlan();
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

        solo.clickOnButton("MEAL PLANS");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", MPMealPlanActivity.class);
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
     * a new meal plan
     */
    @Test
    public void testCreateNewMealPlan() throws InterruptedException {
        solo.assertCurrentActivity("Wrong Activity",MPMealPlanActivity.class);
        int mpBefore = ((RecyclerView) solo.getView(R.id.mp_recyclerview)).getAdapter().getItemCount();
        solo.clickOnView(solo.getView(R.id.my_meals_add_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", MPMealListActivity.class);
        int mealsBefore = ((RecyclerView) solo.getView(R.id.mp_meal_list_recyclerview)).getAdapter().getItemCount();
        // breakfast
        solo.clickOnButton("ADD MEAL");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);
        solo.clickOnView(solo.getView(R.id.my_meals_recyclerview));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MPMealListActivity.class);
        // lunch
        solo.clickOnButton("ADD MEAL");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MPMyMealsActivity.class);
        solo.clickOnView(solo.getView(R.id.my_meals_recyclerview));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MPMealListActivity.class);
        // dinner
        solo.clickOnButton("ADD MEAL");
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MPMyMealsActivity.class);
        solo.clickOnView(solo.getView(R.id.my_meals_recyclerview));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity",MPMealListActivity.class);
        int mealsAfter = ((RecyclerView) solo.getView(R.id.mp_meal_list_recyclerview)).getAdapter().getItemCount();
        assertTrue((mealsAfter-mealsBefore)>0);
        solo.clickOnButton("FINISH");
        int mpAfter = ((RecyclerView) solo.getView(R.id.mp_recyclerview)).getAdapter().getItemCount();
        assertTrue((mpAfter-mpBefore)>0);
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
