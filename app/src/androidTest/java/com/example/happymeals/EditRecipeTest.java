package com.example.happymeals;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.recipe.EditRecipe;
import com.example.happymeals.recipe.RecipeAddIngredient;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditRecipeTest {
    private Solo solo;
    public ActivityTestRule<EditRecipe> rule =
            new ActivityTestRule(EditRecipe.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

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
     * Tests that a new ingredient can be added to a recipe
     */
    @Test
    public void checkList(){
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("RECIPES");
//        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class
//        solo.clickOnButton();
        //Asserts that the current activity is the EditRecipe Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.clickOnButton("PICK A NEW INGREDIENT"); // Click PICK A NEW INGREDIENT Button
        // Assert that the current activity is the RecipeAddIngredient Activity
        solo.assertCurrentActivity("Wrong Activity", RecipeAddIngredient.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_description), "Test");
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_amount), String.valueOf(1));
        solo.clickOnButton("SUBMIT");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.waitForText("Test", 1, 2000);
    }

    /**
     *
     */
    @Test
    public void testDelete() {
        //Asserts that the current activity is the EditRecipe Activity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.clickOnButton("PICK A NEW INGREDIENT"); // Click PICK A NEW INGREDIENT Button
        // Assert that the current activity is the RecipeAddIngredient Activity
        solo.assertCurrentActivity("Wrong Activity", RecipeAddIngredient.class);
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_description), "Test");
        solo.enterText((EditText) solo.getView(R.id.recipe_add_ingredient_amount), String.valueOf(1));
        solo.clickOnButton("SUBMIT");
        solo.assertCurrentActivity("Wrong Activity", EditRecipe.class);
        solo.waitForText("Test");
//        solo.clickOnImageButton(0);
//        assertFalse(solo.searchText("Test"));
    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
