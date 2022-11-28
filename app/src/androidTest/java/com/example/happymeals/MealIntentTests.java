package com.example.happymeals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.ingredient.IngredientActivity;
import com.example.happymeals.ingredient.UserIngredient;
import com.example.happymeals.meal.MPMealRecipeList;
import com.example.happymeals.meal.MPMyMealsActivity;
import com.example.happymeals.meal.MPPickRecipeActivity;
import com.example.happymeals.meal.Meal;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.ViewRecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class MealIntentTests {
    private Solo solo;
    private DBHandler db;
    private FirebaseFirestore conn;
    private Recipe recipe;
    private Meal meal;
    private UserIngredient userIngredient;
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = new DBHandler(user.getUid());

        //set up recipe test data
        recipe = new Recipe("Test Recipe98304", 10, 1, "Test", new ArrayList<>(), new ArrayList<>());
        db.addRecipe(recipe);

        // set up meal test data
        meal = new Meal();
        meal.addRecipe(recipe);
        meal.setScalingForRecipe(recipe,2.0);
        meal.setTitle("Test Meal 98305");
        db.addMeal(meal);

        // set up userIngredient test data
        userIngredient = new UserIngredient("test", "test ingredient9987",12.0,2.0, new Date(), "mockLoc", "g");
        userIngredient.setCost(1.0);
        db.newIngredient(userIngredient);

        // travel to my meals activity
        solo.clickOnView(solo.getView(R.id.meal_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);
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
    public void test_pick_recipes_for_meal() throws InterruptedException {

        // click to add a new meal
        FloatingActionButton addButton = (FloatingActionButton) solo.getView(R.id.my_meals_add_button);
        solo.clickOnView(addButton);

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button addRecipeButton = (Button) solo.getView(R.id.mp_recipe_add_button);
        solo.clickOnView(addRecipeButton);
        solo.waitForDialogToOpen();
        TextView add_new_recipe = (TextView) solo.getView(R.id.bottom_sheet_textview1);
        solo.clickOnView(add_new_recipe);
        solo.assertCurrentActivity("Wrong Activity", MPPickRecipeActivity.class); // within pick

        solo.clickOnText("Test Recipe98304");
        solo.clickOnButton("CONFIRM");

        // ensure the recipe was added to the meal
        solo.waitForActivity(MPMealRecipeList.class);
        solo.waitForText("Test Recipe98304");

        Button cancelButton = (Button) solo.getView(R.id.mpmeal_recipe_list_cancel);
        solo.clickOnView(cancelButton);
        solo.clickOnText("OK");
        solo.waitForActivity(MPMyMealsActivity.class);

        Button finishButton = (Button) solo.getView(R.id.my_meals_finish);
        solo.clickOnView(finishButton);
    }

    /**
     * This test the flow of add a new ingredient to
     * a new meal
     */
    @Test
    public void test_add_new_ingredient(){
        // click to add a new meal
        FloatingActionButton addButton = (FloatingActionButton) solo.getView(R.id.my_meals_add_button);
        solo.clickOnView(addButton);

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button addRecipeButton = (Button) solo.getView(R.id.mp_recipe_add_button);
        solo.clickOnView(addRecipeButton);
        solo.waitForDialogToOpen();
        TextView add_new_recipe = (TextView) solo.getView(R.id.bottom_sheet_textview2);
        solo.clickOnView(add_new_recipe);
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        Button cancelButton = (Button) solo.getView(R.id.mpmeal_recipe_list_cancel);
        solo.clickOnView(cancelButton);
        solo.assertCurrentActivity("Wrong Activity", MPMyMealsActivity.class);
    }

    /**
     * this test the flow of viewing
     * an existing meal
     */
    @Test
    public void test_view_meal_recipe(){
        // click to add a new meal
        solo.waitForText("Test Meal 98305");
        solo.clickOnText("Test Meal 98305");

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        solo.waitForText("Test Recipe98304");
        solo.clickOnText("Test Recipe98304");
        solo.waitForDialogToOpen();
        TextView view_detail = (TextView) solo.getView(R.id.bottom_sheet_textview1);
        solo.clickOnView(view_detail);

        solo.waitForActivity(ViewRecipeActivity.class);
        solo.waitForText("Test Recipe98304");
    }

    /**
     * this test the flow of adjusting
     * the scale for a meal
     */
    @Test
    public void adjust_scaling_for_meal(){
        solo.waitForText("Test Meal 98305");
        solo.clickOnText("Test Meal 98305");

        // view all recipes for this meal
        solo.assertCurrentActivity("Wrong Activity", MPMealRecipeList.class);
        solo.waitForText("Test Recipe98304");
        solo.clickOnText("Test Recipe98304");
        solo.waitForDialogToOpen();
        TextView change_scaling = (TextView) solo.getView(R.id.bottom_sheet_textview2);
        solo.clickOnView(change_scaling);
        solo.waitForDialogToOpen();
        EditText scale_input = (EditText) solo.getView(R.id.editTextScalingNumber);
        solo.clearEditText(scale_input);
        solo.enterText(scale_input,"29.0");
        solo.clickOnButton(2);
        solo.waitForText("29.0");
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
        db.removeRecipe(recipe);
        db.removeMeal(meal);
        db.deleteIngredient(userIngredient);
    }

}
