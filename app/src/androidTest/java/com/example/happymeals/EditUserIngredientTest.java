package com.example.happymeals;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditUserIngredientTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<IngredientActivity> rule = new ActivityTestRule<>(IngredientActivity.class, true, true);

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);
        solo.clickOnView(solo.getView(R.id.floatingAdd));
        solo.assertCurrentActivity("Wrong Activity", AddNewIngredient.class);
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
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "TestDes");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocation), "TestLoc");
        solo.enterText((EditText) solo.getView(R.id.ingredientCount), "2");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitCost), "3.3");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2023, 5, 26);

        solo.clickOnView(solo.getView(R.id.confirm_button));
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.waitForText("TestDes", 1, 2000);
        solo.waitForText("Amount: 2", 1, 2000);
        solo.waitForText("Unit cost: $3.3", 1, 2000);
        solo.waitForText("Bestbefore: 2023-06-26", 1, 2000);
    }

    // This test will only work if there are no ingredients.
    @Test
    public void deleteNewIngredient() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "TestDes");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocation), "TestLoc");
        solo.enterText((EditText) solo.getView(R.id.ingredientCount), "2");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitCost), "3.3");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2023, 5, 26);

        solo.clickOnView(solo.getView(R.id.confirm_button));
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.waitForText("TestDes", 1, 2000);
        solo.waitForText("Amount: 2", 1, 2000);
        solo.waitForText("Unit cost: $3.3", 1, 2000);
        solo.waitForText("Bestbefore: 2023-06-26", 1, 2000);

        solo.clickOnView((ListView) solo.getView(R.id.storage_ingredient_list));

        // Check if the fragment is entered
        solo.waitForFragmentByTag("Food detail", 5000);
        solo.clickOnText("Delete");

    }

    @Test
    public void editNewIngredient() throws Exception{
        solo.enterText((EditText) solo.getView(R.id.ingredientDescription), "TestDes");
        solo.enterText((EditText) solo.getView(R.id.ingredientLocation), "TestLoc");
        solo.enterText((EditText) solo.getView(R.id.ingredientCount), "2");
        solo.enterText((EditText) solo.getView(R.id.ingredientUnitCost), "3.3");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2023, 5, 26);

        solo.clickOnView(solo.getView(R.id.confirm_button));
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class);

        solo.clickOnView((ListView) solo.getView(R.id.storage_ingredient_list));

        // Check if the fragment is entered
        solo.waitForFragmentByTag("Food detail", 5000);

        solo.clearEditText((EditText) solo.getView(R.id.description_frag));
        solo.enterText((EditText) solo.getView(R.id.description_frag), "TestDesEdit");
        solo.clearEditText((EditText) solo.getView(R.id.count_frag));
        solo.enterText((EditText) solo.getView(R.id.count_frag), "5");
        solo.clearEditText((EditText) solo.getView(R.id.unitcost_frag));
        solo.enterText((EditText) solo.getView(R.id.unitcost_frag), "7.8");
        solo.setDatePicker((DatePicker) solo.getView(R.id.ingredientBestbefore), 2024, 7, 5);

        solo.clickOnText("Confirm");

        solo.waitForText("TestDesEdit", 1, 2000);
        solo.waitForText("Amount: 5", 1, 2000);
        solo.waitForText("Unit cost: $7.8", 1, 2000);
        solo.waitForText("Bestbefore: 2024-08-5", 1, 2000);

    }
}