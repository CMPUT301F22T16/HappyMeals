package com.example.happymeals;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.happymeals.ingredient.AddNewIngredient;
import com.example.happymeals.ingredient.IngredientActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditUserIngredientTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.ingredient_button));
        solo.sleep(3000);
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void addNewIngredient() throws Exception{
        solo.clickOnView(solo.getView(R.id.floatingAdd));
        solo.assertCurrentActivity("Wrong Activity", AddNewIngredient.class);
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "TestDes");

        solo.clickOnView(solo.getView(R.id.ingredientLocation));
        solo.clickOnText("Add new location");
        solo.waitForFragmentByTag("Add new storage location", 3000);
        solo.enterText((EditText) solo.getView(R.id.location), "TestLoc");
        solo.clickOnText("Confirm");

        solo.clickOnView(solo.getView(R.id.ingredientLocation));
        solo.clickOnText("TestLoc");
        solo.clickOnView(solo.getView(R.id.ingredientCategory));
        solo.clickOnText("Drink");
        solo.clickOnView(solo.getView(R.id.ingredientUnit));
        solo.clickOnText("ml");
        //solo.enterText((EditText) solo.getView(R.id.ingredientLocation), "TestLoc");
        solo.enterText((EditText) solo.getView(R.id.ingredientCount), "2");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitCost), "3.3");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2023, 5, 26);


        solo.clickOnView(solo.getView(R.id.confirm_button));
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.waitForText("TestDes", 1, 2000);
        solo.waitForText("2.0 ml", 1, 2000);
        solo.waitForText("$3.3", 1, 2000);
        solo.waitForText("Bestbefore: 2023-06-26", 1, 2000);
    }

    @Test
    public void deleteNewIngredient() throws Exception{
        addNewIngredient();
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.clickOnText("TestDes");

        // Check if the fragment is entered
        solo.waitForFragmentByTag("Food detail", 3000);
        solo.clickOnText("Delete");
        solo.waitForFragmentByTag("Confirm deletion", 3000);
        solo.clickOnText("Yes");

    }

    @Test
    public void editNewIngredient() throws Exception{
        addNewIngredient();
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.clickOnText("TestDes");

        // Check if the fragment is entered
        solo.waitForFragmentByTag("Food detail", 3000);

        solo.clearEditText((EditText) solo.getView(R.id.description_frag));
        solo.enterText((EditText) solo.getView(R.id.description_frag), "TestDesEdit");
        solo.clickOnView(solo.getView(R.id.category));
        solo.clickOnText("Fruit");
        solo.clickOnView(solo.getView(R.id.unit_frag));
        solo.clickOnText("kg");
        solo.clearEditText((EditText) solo.getView(R.id.count_frag));
        solo.enterText((EditText) solo.getView(R.id.count_frag), "5");
        solo.clearEditText((EditText) solo.getView(R.id.unitcost_frag));
        solo.enterText((EditText) solo.getView(R.id.unitcost_frag), "7.8");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2024, 7, 5);

        solo.clickOnText("Confirm");

        solo.waitForText("TestDesEdit", 1, 2000);
        solo.waitForText("5.0 kg", 1, 2000);
        solo.waitForText("$7.8", 1, 2000);
        solo.waitForText("Bestbefore: 2024-08-5", 1, 2000);

    }
}